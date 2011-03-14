package sk.myshop.app.client.navigation;

import sk.myshop.app.client.MyShopInjector;
import sk.myshop.app.client.presenter.Presenter;

public interface PresenterProvider {

    Presenter get(MyShopInjector injector);

}
