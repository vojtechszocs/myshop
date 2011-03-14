package sk.myshop.app.client.command;

import sk.myshop.command.client.CommandResultHandler;
import sk.myshop.command.client.EventBus;

import com.google.gwt.user.client.Window;
import com.google.inject.Inject;

public class MessageResultHandler implements CommandResultHandler<MessageResult> {

    @Inject
    public MessageResultHandler(EventBus eventBus) {
        eventBus.addHandler(MessageResult.getType(), this);
    }

    public void onResult(MessageResult result) {
        Window.alert(result.getMessage());
    }

}
