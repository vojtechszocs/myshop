package sk.myshop.app.server;

import sk.myshop.command.client.Command;
import sk.myshop.command.client.CommandService;
import sk.myshop.command.client.Result;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Singleton;

@Singleton
public class CommandServiceImpl extends RemoteServiceServlet implements CommandService {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    public <T extends Result> T executeCommand(Command<T> cmd) {
        if (Executable.class.isAssignableFrom(cmd.getClass()))
            return ((Executable<T>) cmd).execute();

        throw new RuntimeException("Command implementation class must implement Executable interface: " + cmd.getClass().getCanonicalName());
    }

}
