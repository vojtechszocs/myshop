package sk.myshop.command.client;

import com.google.gwt.event.shared.EventHandler;

public interface CommandHandler extends EventHandler {

    <T extends ResultEvent<?>> void onExecute(CommandEvent<T> commandEvent);

}
