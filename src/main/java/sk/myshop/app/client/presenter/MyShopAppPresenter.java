package sk.myshop.app.client.presenter;

import sk.myshop.app.client.event.NavigationEvent;
import sk.myshop.app.client.event.NavigationEventHandler;
import sk.myshop.app.client.presenter.mapping.MyShopPresenterMapping;
import sk.myshop.app.client.view.MyShopAppView;
import sk.myshop.command.client.EventBus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;

/**
 * Central application presenter.
 */
public class MyShopAppPresenter extends PresenterAdapter implements ValueChangeHandler<String> {

    @ImplementedBy(MyShopAppView.class)
    public interface Display {
        Widget asWidget();

        HasWidgets headerNavigationPanel();

        HasWidgets contentPanel();
    }

    private final EventBus eventBus;
    private final Display view;
    private final MyShopPresenterMapping presenterMapping;

    private Presenter presenter;

    @Inject
    public MyShopAppPresenter(EventBus eventBus, Display view, MyShopPresenterMapping presenterMapping,
            HeaderNavigationPresenter headerNavigationPresenter) {
        this.eventBus = eventBus;
        this.view = view;
        this.presenterMapping = presenterMapping;
        headerNavigationPresenter.go(view.headerNavigationPanel());

        registerHandlers();
        History.addValueChangeHandler(this);
        History.fireCurrentHistoryState();
    }

    private void registerHandlers() {
        register(eventBus.addHandler(NavigationEvent.TYPE, new NavigationEventHandler() {
            @Override
            public void onHistoryToken(String token) {
                History.newItem(token);
            }
        }));
    }

    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(view.asWidget());
    }

    @Override
    public void onValueChange(final ValueChangeEvent<String> event) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onSuccess() {
                loadPresenter(event.getValue());
            }

            @Override
            public void onFailure(Throwable reason) {
                Window.alert("Error: " + reason.getMessage());
            }
        });
    }

    private void loadPresenter(String historyToken) {
        disposePresenter(presenter);

        presenter = presenterMapping.getPresenter(historyToken);
        if (presenter != null)
            presenter.go(view.contentPanel());
    }

    @Override
    public void dispose() {
        disposePresenter(presenter);
        super.dispose();
    }

}
