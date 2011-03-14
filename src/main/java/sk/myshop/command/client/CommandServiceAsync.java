package sk.myshop.command.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CommandServiceAsync {

    <T extends Result> void executeCommand(Command<T> cmd, AsyncCallback<T> callback);

}
