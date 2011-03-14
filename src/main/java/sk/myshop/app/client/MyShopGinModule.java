package sk.myshop.app.client;

import sk.myshop.app.client.command.MessageResultHandler;
import sk.myshop.app.client.navigation.HistoryTokenParameterProvider;
import sk.myshop.app.client.navigation.Place;
import sk.myshop.app.client.navigation.merchant.MerchantNavigation;
import sk.myshop.app.client.navigation.merchant.ShopId;
import sk.myshop.command.client.RpcCommandInvoker;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;

public class MyShopGinModule extends AbstractGinModule {

    @Override
    protected void configure() {
        bind(RpcCommandInvoker.class).asEagerSingleton();
        bind(MessageResultHandler.class).asEagerSingleton();
    }

    Long getHistoryTokenParameterAsLong(Place place, String parameterName) {
        return HistoryTokenParameterProvider.longProvider(place.getHistoryTokenPattern(), parameterName).get();
    }

    String getHistoryTokenParameter(Place place, String parameterName) {
        return HistoryTokenParameterProvider.stringProvider(place.getHistoryTokenPattern(), parameterName).get();
    }

    @Provides
    @ShopId
    String shopId() {
        return getHistoryTokenParameter(MerchantNavigation.shopDetail, ShopId.NAME);
    }

}
