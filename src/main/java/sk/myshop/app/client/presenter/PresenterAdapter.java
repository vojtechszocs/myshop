package sk.myshop.app.client.presenter;

import java.util.HashSet;

import com.google.gwt.event.shared.HandlerRegistration;

public abstract class PresenterAdapter implements Presenter {

    @SuppressWarnings("serial")
    private static class HandlerRegistrationUtil extends HashSet<HandlerRegistration> {
        public void dispose() {
            for (HandlerRegistration handler : this)
                handler.removeHandler();
        }
    }

    private final HandlerRegistrationUtil handlers = new HandlerRegistrationUtil();

    protected void register(HandlerRegistration handler) {
        handlers.add(handler);
    }

    public void dispose() {
        handlers.dispose();
    }

    protected void disposePresenter(Presenter presenter) {
        if (presenter != null)
            presenter.dispose();
    }

}
