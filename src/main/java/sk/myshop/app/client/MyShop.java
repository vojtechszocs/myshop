package sk.myshop.app.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

public class MyShop implements EntryPoint {

    private static final MyShopInjector injector = GWT.create(MyShopInjector.class);

    public void onModuleLoad() {
        injector.appPresenter().go(RootPanel.get());
    }

}
