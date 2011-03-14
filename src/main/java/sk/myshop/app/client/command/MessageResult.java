package sk.myshop.app.client.command;

import sk.myshop.command.client.CommandResultHandler;
import sk.myshop.command.client.ResultEvent;

public class MessageResult extends ResultEvent<CommandResultHandler<MessageResult>> {

    private static Type<CommandResultHandler<MessageResult>> TYPE;

    public static Type<CommandResultHandler<MessageResult>> getType() {
        return TYPE != null ? TYPE : (TYPE = new Type<CommandResultHandler<MessageResult>>());
    }

    @Override
    public Type<CommandResultHandler<MessageResult>> getAssociatedType() {
        return getType();
    }

    private String messsage;

    protected MessageResult() {
    }

    public MessageResult(String msg) {
        this.messsage = msg;
    }

    public String getMessage() {
        return messsage;
    }

}
