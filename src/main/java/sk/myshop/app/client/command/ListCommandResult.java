package sk.myshop.app.client.command;

import java.util.Collections;
import java.util.List;

import sk.myshop.command.client.CommandResultHandler;
import sk.myshop.command.client.ResultEvent;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("unchecked")
public abstract class ListCommandResult<T extends CommandResultHandler, I extends IsSerializable> extends ResultEvent<T> {

    private List<I> items;
    private String keyBookmark;

    protected ListCommandResult() {
    }

    protected ListCommandResult(List<I> items) {
        this.items = items;
    }

    protected ListCommandResult(List<I> items, String keyBookmark) {
        this(items);
        this.keyBookmark = keyBookmark;
    }

    public List<I> getItems() {
        return Collections.unmodifiableList(items);
    }

    public String getKeyBookmark() {
        return keyBookmark;
    }

    public boolean hasNext() {
        return keyBookmark != null;
    }

}
