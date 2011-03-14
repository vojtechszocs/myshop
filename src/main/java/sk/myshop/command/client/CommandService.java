package sk.myshop.command.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath(CommandService.PATH)
public interface CommandService extends RemoteService {

    public static final String PATH = "gwt.rpc";

    <T extends Result> T executeCommand(Command<T> cmd);

}
