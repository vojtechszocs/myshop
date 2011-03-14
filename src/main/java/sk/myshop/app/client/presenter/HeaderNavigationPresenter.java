package sk.myshop.app.client.presenter;

import sk.myshop.app.client.navigation.merchant.MerchantNavigation;
import sk.myshop.app.client.view.HeaderNavigationView;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;

public class HeaderNavigationPresenter extends PresenterAdapter {

    @ImplementedBy(HeaderNavigationView.class)
    public interface Display {
        Widget asWidget();

        void setHomeLinkToken(String token);

        void setShopsLinkToken(String token);

        void setProductsLinkToken(String token);

        void setGalleryLinkToken(String token);

        void setAccountLinkToken(String token);
    }

    private final Display view;

    @Inject
    public HeaderNavigationPresenter(Display view) {
        this.view = view;
        initLinks();
    }

    private void initLinks() {
        view.setHomeLinkToken(MerchantNavigation.homeEvent().getHistoryToken());
        view.setShopsLinkToken(MerchantNavigation.shopsEvent().getHistoryToken());
        view.setProductsLinkToken(MerchantNavigation.productsEvent().getHistoryToken());
        view.setGalleryLinkToken(MerchantNavigation.galleryEvent().getHistoryToken());
        view.setAccountLinkToken(MerchantNavigation.accountEvent().getHistoryToken());
    }

    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(view.asWidget());
    }

}
