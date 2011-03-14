package sk.myshop.app.client.navigation.merchant;

import sk.myshop.app.client.MyShopInjector;
import sk.myshop.app.client.event.NavigationEvent;
import sk.myshop.app.client.navigation.Place;
import sk.myshop.app.client.presenter.Presenter;

public enum MerchantNavigation implements Place {

    home("home") {
        @Override
        public Presenter get(MyShopInjector injector) {
            return injector.homePresenter();
        }
    },

    shops("shops") { // TODO shops/:filter, e.g. shops/all or shops/by-city/Bratislava
        @Override
        public Presenter get(MyShopInjector injector) {
            return injector.shopsPresenter();
        }
    },

    shopDetail("shops/:" + ShopId.NAME) {
        @Override
        public Presenter get(MyShopInjector injector) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    products("products") {
        @Override
        public Presenter get(MyShopInjector injector) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    gallery("gallery") {
        @Override
        public Presenter get(MyShopInjector injector) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    account("account") {
        @Override
        public Presenter get(MyShopInjector injector) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    ;

    private final String historyTokenPattern;

    MerchantNavigation(String historyTokenPattern) {
        this.historyTokenPattern = historyTokenPattern;
    }

    public String getHistoryTokenPattern() {
        return historyTokenPattern;
    }

    @Override
    public abstract Presenter get(MyShopInjector injector);

    public static NavigationEvent homeEvent() {
        return new NavigationEvent(home);
    }

    public static NavigationEvent shopsEvent() {
        return new NavigationEvent(shops);
    }

    public static NavigationEvent shopDetailEvent(final String shopId) {
        return new NavigationEvent(shopDetail) {
            {
                setParameter(ShopId.NAME, shopId);
            }
        };
    }

    public static NavigationEvent productsEvent() {
        return new NavigationEvent(products);
    }

    public static NavigationEvent galleryEvent() {
        return new NavigationEvent(gallery);
    }

    public static NavigationEvent accountEvent() {
        return new NavigationEvent(account);
    }

}
