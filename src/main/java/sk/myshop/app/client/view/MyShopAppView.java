package sk.myshop.app.client.view;

import sk.myshop.app.client.presenter.MyShopAppPresenter.Display;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class MyShopAppView extends Composite implements Display {

    interface Binder extends UiBinder<Widget, MyShopAppView> {
        static final Binder binder = GWT.create(Binder.class);
    }

    @UiField
    FlowPanel headerNavigationPanel;

    @UiField
    FlowPanel contentPanel;

    @Inject
    public MyShopAppView() {
        initWidget(Binder.binder.createAndBindUi(this));
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public HasWidgets headerNavigationPanel() {
        return headerNavigationPanel;
    }

    @Override
    public HasWidgets contentPanel() {
        return contentPanel;
    }

}
