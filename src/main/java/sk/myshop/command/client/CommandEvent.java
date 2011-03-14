package sk.myshop.command.client;

import com.google.gwt.event.shared.GwtEvent;

public class CommandEvent<T extends ResultEvent<?>> extends GwtEvent<CommandHandler> implements Command<T> {

    private static Type<CommandHandler> TYPE;

    public static Type<CommandHandler> getType() {
        return TYPE != null ? TYPE : (TYPE = new Type<CommandHandler>());
    }

    @Override
    public final Type<CommandHandler> getAssociatedType() {
        return getType();
    }

    @Override
    protected void dispatch(CommandHandler commandHandler) {
        commandHandler.onExecute(this);
    }

}
