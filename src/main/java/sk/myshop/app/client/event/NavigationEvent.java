package sk.myshop.app.client.event;

import sk.myshop.app.client.navigation.HistoryTokenBuilder;
import sk.myshop.app.client.navigation.Place;

import com.google.gwt.event.shared.GwtEvent;

public class NavigationEvent extends GwtEvent<NavigationEventHandler> {

    public static Type<NavigationEventHandler> TYPE = new Type<NavigationEventHandler>();

    private final HistoryTokenBuilder tokenBuilder;

    public NavigationEvent(Place place) {
        this.tokenBuilder = new HistoryTokenBuilder(place.getHistoryTokenPattern());
    }

    protected void setParameter(String name, String value) {
        tokenBuilder.set(name, value);
    }

    protected void setParameter(String name, Long value) {
        setParameter(name, String.valueOf(value));
    }

    @Override
    public final Type<NavigationEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(NavigationEventHandler handler) {
        handler.onHistoryToken(getHistoryToken());
    }

    public String getHistoryToken() {
        return tokenBuilder.build();
    }

}
