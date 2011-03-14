package sk.myshop.app.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface NavigationEventHandler extends EventHandler {

    void onHistoryToken(String token);

}
