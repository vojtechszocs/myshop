package sk.myshop.app.client.view;

import sk.myshop.app.client.presenter.HomePresenter.Display;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class HomeView extends Composite implements Display {

    interface Binder extends UiBinder<Widget, HomeView> {
        static final Binder binder = GWT.create(Binder.class);
    }

    @Inject
    public HomeView() {
        initWidget(Binder.binder.createAndBindUi(this));
    }

    @Override
    public Widget asWidget() {
        return this;
    }

}
