package sk.myshop.app.client.presenter.mapping;

import sk.myshop.app.client.MyShopInjector;
import sk.myshop.app.client.navigation.Place;
import sk.myshop.app.client.navigation.merchant.MerchantNavigation;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class MyShopPresenterMapping extends PresenterMappingAdapter {

    @Inject
    public MyShopPresenterMapping(MyShopInjector injector) {
        super(injector);
        init();
    }

    private void init() {
        for (Place place : MerchantNavigation.values())
            addPlace(place);
    }

    @Override
    protected Place getDefaultPlace() {
        return MerchantNavigation.home;
    }

}
