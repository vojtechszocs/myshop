package sk.myshop.app.client.view;

import sk.myshop.app.client.presenter.HeaderNavigationPresenter.Display;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class HeaderNavigationView extends Composite implements Display {

    interface Binder extends UiBinder<Widget, HeaderNavigationView> {
        static final Binder binder = GWT.create(Binder.class);
    }

    @UiField
    InlineHyperlink homeLink;

    @UiField
    InlineHyperlink shopsLink;

    @UiField
    InlineHyperlink productsLink;

    @UiField
    InlineHyperlink galleryLink;

    @UiField
    InlineHyperlink accountLink;

    @Inject
    public HeaderNavigationView() {
        initWidget(Binder.binder.createAndBindUi(this));
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public void setHomeLinkToken(String token) {
        homeLink.setTargetHistoryToken(token);
    }

    @Override
    public void setShopsLinkToken(String token) {
        shopsLink.setTargetHistoryToken(token);
    }

    @Override
    public void setProductsLinkToken(String token) {
        productsLink.setTargetHistoryToken(token);
    }

    @Override
    public void setGalleryLinkToken(String token) {
        galleryLink.setTargetHistoryToken(token);
    }

    @Override
    public void setAccountLinkToken(String token) {
        accountLink.setTargetHistoryToken(token);
    }

}
