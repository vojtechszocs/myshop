package sk.myshop.app.client.presenter.mapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import sk.myshop.app.client.MyShopInjector;
import sk.myshop.app.client.navigation.HistoryTokenPatternMatcher;
import sk.myshop.app.client.navigation.Place;
import sk.myshop.app.client.navigation.PresenterProvider;
import sk.myshop.app.client.presenter.Presenter;

public abstract class PresenterMappingAdapter implements PresenterMapping {

    private final Map<HistoryTokenPatternMatcher, PresenterProvider> mappings = new HashMap<HistoryTokenPatternMatcher, PresenterProvider>();
    private final MyShopInjector injector;

    public PresenterMappingAdapter(MyShopInjector injector) {
        this.injector = injector;
    }

    protected void addPlace(Place place) {
        mappings.put(new HistoryTokenPatternMatcher(place.getHistoryTokenPattern()), place);
    }

    protected abstract Place getDefaultPlace();

    public Presenter getPresenter(String historyToken) {
        Set<HistoryTokenPatternMatcher> matchers = mappings.keySet();

        for (HistoryTokenPatternMatcher matcher : matchers) {
            if (matcher.matches(historyToken))
                return mappings.get(matcher).get(injector);
        }

        PresenterProvider defaultProvider = getDefaultPlace();
        return defaultProvider != null ? defaultProvider.get(injector) : null;
    }

}
