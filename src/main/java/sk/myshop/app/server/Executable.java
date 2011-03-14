package sk.myshop.app.server;

import sk.myshop.command.client.Result;

public interface Executable<T extends Result> {

    T execute();

}
