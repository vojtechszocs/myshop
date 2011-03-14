package sk.myshop.app.client.presenter;

import sk.myshop.app.client.command.ListShopsResult.ShopListItem;
import sk.myshop.app.client.navigation.merchant.MerchantNavigation;
import sk.myshop.app.client.view.ShopListItemView;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;

public class ShopListItemPresenter extends PresenterAdapter {

    @ImplementedBy(ShopListItemView.class)
    public interface Display {
        Widget asWidget();

        void hideContactDetails();

        void setItem(ShopListItem item);

        void setNameLinkToken(String token);

        void setRelatedProductsLinkToken(String token);

        void setDetailLinkToken(String token);
    }

    private final Display view;

    @Inject
    public ShopListItemPresenter(Display view) {
        this.view = view;
        view.hideContactDetails();
    }

    public void setItem(ShopListItem item) {
        view.setItem(item);
        initLinks(item);
    }

    private void initLinks(ShopListItem item) {
        String shopDetailHistoryToken = MerchantNavigation.shopDetailEvent(item.getId()).getHistoryToken();
        view.setNameLinkToken(shopDetailHistoryToken);
        view.setDetailLinkToken(shopDetailHistoryToken);
        view.setRelatedProductsLinkToken("TODO"); // TODO
    }

    @Override
    public void go(HasWidgets container) {
        container.add(view.asWidget());
    }

}
