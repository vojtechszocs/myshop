package sk.myshop.app.client.command;

import sk.myshop.command.client.CommandEvent;
import sk.myshop.command.client.ResultEvent;

public abstract class ListCommand<T extends ResultEvent<?>> extends CommandEvent<T> {

    private int pageSize;
    private String keyBookmark;

    protected ListCommand() {
    }

    protected ListCommand(int pageSize) {
        this.pageSize = pageSize;
    }

    protected ListCommand(int pageSize, String keyBookmark) {
        this(pageSize);
        this.keyBookmark = keyBookmark;
    }

    public int getPageSize() {
        return pageSize;
    }

    public String getKeyBookmark() {
        return keyBookmark;
    }

}
