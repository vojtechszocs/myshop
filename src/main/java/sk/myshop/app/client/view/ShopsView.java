package sk.myshop.app.client.view;

import sk.myshop.app.client.presenter.ShopsPresenter.Display;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ShopsView extends Composite implements Display {

    interface Binder extends UiBinder<Widget, ShopsView> {
        static final Binder binder = GWT.create(Binder.class);
    }

    @UiField
    Button resetPageButton;

    @UiField
    Button previousPageButton;

    @UiField
    Button nextPageButton;

    @UiField
    FlowPanel shopListPanel;

    @Inject
    public ShopsView() {
        initWidget(Binder.binder.createAndBindUi(this));
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public HasWidgets shopListPanel() {
        return shopListPanel;
    }

    @Override
    public HasClickHandlers resetPageButton() {
        return resetPageButton;
    }

    @Override
    public HasClickHandlers previousPageButton() {
        return previousPageButton;
    }

    @Override
    public HasClickHandlers nextPageButton() {
        return nextPageButton;
    }

    @Override
    public void disablePageButtons() {
        setPageButtonsEnabled(false, false, false);
    }

    @Override
    public void setPageButtonsEnabled(boolean resetButtonEnabled, boolean previousButtonEnabled, boolean nextButtonEnabled) {
        resetPageButton.setEnabled(resetButtonEnabled);
        previousPageButton.setEnabled(previousButtonEnabled);
        nextPageButton.setEnabled(nextButtonEnabled);
    }

}
