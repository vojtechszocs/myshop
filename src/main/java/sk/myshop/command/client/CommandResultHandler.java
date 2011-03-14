package sk.myshop.command.client;

import com.google.gwt.event.shared.EventHandler;

public interface CommandResultHandler<T extends Result> extends EventHandler {

    void onResult(T result);

}
