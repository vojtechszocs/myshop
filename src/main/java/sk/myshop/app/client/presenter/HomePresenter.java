package sk.myshop.app.client.presenter;

import sk.myshop.app.client.view.HomeView;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;

public class HomePresenter extends PresenterAdapter {

    @ImplementedBy(HomeView.class)
    public interface Display {
        Widget asWidget();
    }

    private final Display view;

    @Inject
    public HomePresenter(Display view) {
        this.view = view;
    }

    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(view.asWidget());
    }

}
