package sk.myshop.command.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class RpcCommandInvoker implements CommandHandler {

    private final EventBus eventBus;
    private final CommandServiceAsync cmdService;

    @Inject
    public RpcCommandInvoker(EventBus eventBus, CommandServiceAsync cmdService) {
        this.eventBus = eventBus;
        this.cmdService = cmdService;
        this.eventBus.addHandler(CommandEvent.getType(), this);
    }

    public <T extends ResultEvent<?>> void onExecute(CommandEvent<T> commandEvent) {
        cmdService.executeCommand(commandEvent, new AsyncCallback<T>() {
            public void onFailure(Throwable caught) {
                Window.alert("Error: " + caught);
            }

            public void onSuccess(T result) {
                eventBus.fireEvent(result);
            }
        });
    }

}
