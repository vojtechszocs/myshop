package sk.myshop.command.client;

import com.google.gwt.event.shared.GwtEvent;

@SuppressWarnings("unchecked")
public abstract class ResultEvent<T extends CommandResultHandler> extends GwtEvent<T> implements Result {

    @Override
    protected void dispatch(T handler) {
        handler.onResult(this);
    }

}
