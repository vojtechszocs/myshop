package sk.myshop.app.client.view;

import sk.myshop.app.client.command.ListShopsResult.ShopListItem;
import sk.myshop.app.client.presenter.ShopListItemPresenter.Display;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class ShopListItemView extends Composite implements Display {

    interface Binder extends UiBinder<Widget, ShopListItemView> {
        static final Binder binder = GWT.create(Binder.class);
    }

    @UiField
    Image image;

    @UiField
    HTMLPanel streetPanel;

    @UiField
    HTMLPanel cityAndZipPanel;

    @UiField
    HTMLPanel emailPanel;

    @UiField
    Anchor emailLink;

    @UiField
    HTMLPanel phonePanel;

    @UiField
    InlineHyperlink nameLink;

    @UiField
    InlineLabel descriptionLabel;

    @UiField
    InlineHyperlink relatedProductsLink;

    @UiField
    InlineHyperlink detailLink;

    @Inject
    public ShopListItemView() {
        initWidget(Binder.binder.createAndBindUi(this));
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public void hideContactDetails() {
        streetPanel.setVisible(false);
        cityAndZipPanel.setVisible(false);
        emailPanel.setVisible(false);
        phonePanel.setVisible(false);
    }

    @Override
    public void setItem(ShopListItem item) {
        if (item.getImageUrl() != null)
            image.setUrl(item.getImageUrl());

        streetPanel.getElement().setInnerText(item.getStreet());
        streetPanel.setVisible(item.getStreet() != null);

        String cityAndZip = (item.getZip() != null) ? item.getZip() + " | " + item.getCity() : item.getCity();
        cityAndZipPanel.getElement().setInnerText(cityAndZip);
        cityAndZipPanel.setVisible(true);

        emailPanel.setVisible(true);
        emailLink.setText(item.getEmail());
        emailLink.setHref("mailto:" + item.getEmail());

        phonePanel.getElement().setInnerText(item.getPhone());
        phonePanel.setVisible(item.getPhone() != null);

        nameLink.setText(item.getName());
        descriptionLabel.setText(item.getDescription());

        relatedProductsLink.setText("Produkty (" + item.getRelatedProductCount() + ")");
    }

    @Override
    public void setNameLinkToken(String token) {
        nameLink.setTargetHistoryToken(token);
    }

    @Override
    public void setRelatedProductsLinkToken(String token) {
        relatedProductsLink.setTargetHistoryToken(token);
    }

    @Override
    public void setDetailLinkToken(String token) {
        detailLink.setTargetHistoryToken(token);
    }

}
