package sk.myshop.app.server.domain;

import java.util.Collections;
import java.util.List;

/**
 * Page query result.
 */
public class PageResult<T extends ModelObject<?>, B> {

    private final List<T> items;
    private final B bookmark;

    PageResult(List<T> items, B bookmark) {
        this.items = items;
        this.bookmark = bookmark;
    }

    /**
     * @return Unmodifiable collection.
     */
    public List<T> getItems() {
        return Collections.unmodifiableList(items);
    }

    /**
     * Used to resume page queries.
     * <p>
     * Next page starts with an entity with this bookmark.
     */
    public B getBookmark() {
        return bookmark;
    }

    /**
     * @return {@code true} if there are items beyond this page.
     */
    public boolean hasNext() {
        return bookmark != null;
    }

}
