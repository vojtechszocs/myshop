package sk.myshop.app.client.command;

import sk.myshop.app.server.command.ListShopsCommand;
import sk.myshop.command.aspect.Replaceable;

@Replaceable(ListShopsCommand.class)
public class ListShops extends ListCommand<ListShopsResult> {

    protected ListShops() {
    }

    public ListShops(int pageSize, String keyBookmark) {
        super(pageSize, keyBookmark);
    }

}
