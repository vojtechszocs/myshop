package sk.myshop.app.client;

import sk.myshop.app.client.presenter.HomePresenter;
import sk.myshop.app.client.presenter.MyShopAppPresenter;
import sk.myshop.app.client.presenter.ShopsPresenter;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(MyShopGinModule.class)
public interface MyShopInjector extends Ginjector {

    MyShopAppPresenter appPresenter();

    HomePresenter homePresenter();

    ShopsPresenter shopsPresenter();

}
