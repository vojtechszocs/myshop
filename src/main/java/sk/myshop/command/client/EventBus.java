package sk.myshop.command.client;

import com.google.gwt.event.shared.HandlerManager;
import com.google.inject.Singleton;

@Singleton
public class EventBus extends HandlerManager {

    public EventBus() {
        super(null);
    }

}
