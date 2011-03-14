package sk.myshop.app.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;

public interface Presenter {

    /**
     * Render View widget into the given container.
     */
    void go(HasWidgets container);

    /**
     * Detach all handlers associated with this Presenter from corresponding
     * handler managers (View widgets or EventBus).
     */
    void dispose();

}
