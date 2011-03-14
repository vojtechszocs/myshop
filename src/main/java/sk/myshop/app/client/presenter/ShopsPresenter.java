package sk.myshop.app.client.presenter;

import java.util.ArrayList;
import java.util.List;

import sk.myshop.app.client.command.ListShops;
import sk.myshop.app.client.command.ListShopsResult;
import sk.myshop.app.client.command.ListShopsResult.ShopListItem;
import sk.myshop.app.client.view.ShopsView;
import sk.myshop.command.client.CommandResultHandler;
import sk.myshop.command.client.EventBus;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ShopsPresenter extends PresenterAdapter implements CommandResultHandler<ListShopsResult> {

    @ImplementedBy(ShopsView.class)
    public interface Display {
        Widget asWidget();

        HasWidgets shopListPanel();

        HasClickHandlers resetPageButton();

        HasClickHandlers previousPageButton();

        HasClickHandlers nextPageButton();

        void disablePageButtons();

        void setPageButtonsEnabled(boolean resetButtonEnabled, boolean previousButtonEnabled, boolean nextButtonEnabled);
    }

    private final EventBus eventBus;
    private final Display view;
    private final Provider<ShopListItemPresenter> shopProvider;

    private final int pageSize = 4;
    private final List<String> bookmarks = new ArrayList<String>();
    private boolean ready = true;

    @Inject
    public ShopsPresenter(EventBus eventBus, Display view, Provider<ShopListItemPresenter> shopProvider) {
        this.eventBus = eventBus;
        this.view = view;
        this.shopProvider = shopProvider;
        registerCommandHandlers();
        registerWidgetHandlers();
    }

    private void registerCommandHandlers() {
        register(eventBus.addHandler(ListShopsResult.getType(), this));
    }

    private void registerWidgetHandlers() {
        register(view.resetPageButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                requestData(pageSize, null);
            }
        }));
        register(view.previousPageButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (hasPreviousBookmark())
                    requestData(pageSize, getPreviousBookmark());
            }
        }));
        register(view.nextPageButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (hasNextBookmark())
                    requestData(pageSize, getNextBookmark());
            }
        }));
    }

    @Override
    public void onResult(ListShopsResult result) {
        bookmarks.add(result.getKeyBookmark());

        view.setPageButtonsEnabled(true, hasPreviousBookmark(), hasNextBookmark());

        view.shopListPanel().clear();
        for (ShopListItem item : result.getItems())
            addShop(item);

        ready = true;
    }

    private void addShop(ShopListItem item) {
        ShopListItemPresenter shopPresenter = shopProvider.get();
        shopPresenter.setItem(item);
        shopPresenter.go(view.shopListPanel());
    }

    private boolean hasPreviousBookmark() {
        return bookmarks.size() > 1;
    }

    private String getPreviousBookmark() {
        return bookmarks.size() > 2 ? bookmarks.get(bookmarks.size() - 3) : null;
    }

    private boolean hasNextBookmark() {
        return bookmarks.size() > 0 && bookmarks.get(bookmarks.size() - 1) != null;
    }

    private String getNextBookmark() {
        return bookmarks.get(bookmarks.size() - 1);
    }

    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(view.asWidget());
        requestData(pageSize, null);
    }

    private void requestData(int pageSize, String keyBookmark) {
        if (ready) {
            ready = false;
            view.disablePageButtons();

            if (keyBookmark == null) {
                bookmarks.clear();
            } else if (bookmarks.indexOf(keyBookmark) < bookmarks.size() - 1) {
                bookmarks.subList(bookmarks.indexOf(keyBookmark) + 1, bookmarks.size()).clear();
            }

            eventBus.fireEvent(new ListShops(pageSize, keyBookmark));
        }
    }

}
