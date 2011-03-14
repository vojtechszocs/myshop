package sk.myshop.app.server.domain;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;

/**
 * Key-based page query.
 */
public class KeyBasedPageQuery<T extends ModelObject<?>> extends PageQuery<T, Key> {

    KeyBasedPageQuery(int pageSize, Key bookmark, Class<T> entityClass, PersistenceManager persistenceManager) {
        super(pageSize, bookmark, true, entityClass, persistenceManager);
    }

    @Override
    protected Key extractBookmark(T modelObject) {
        return modelObject.getKey();
    }

    @Override
    protected String getBookmarkFieldName() {
        return "key";
    }

}
