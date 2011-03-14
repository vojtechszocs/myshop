package sk.myshop.app.client.presenter.mapping;

import sk.myshop.app.client.presenter.Presenter;

public interface PresenterMapping {

    Presenter getPresenter(String historyToken);
}
