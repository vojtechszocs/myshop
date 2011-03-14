package sk.myshop.app.server.domain;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.orm.jdo.PersistenceManagerFactoryUtils;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * Root of all domain model objects.
 */
@Configurable
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class ModelObject<T extends ModelObject<?>> {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Autowired
    @Transient
    private PersistenceManagerFactory pmf;

    public Key getKey() {
        ensurePersistent();
        return key;
    }

    public String getKeyAsEncodedString() {
        return KeyFactory.keyToString(getKey());
    }

    protected boolean isPersistent() {
        return key != null;
    }

    private void ensurePersistent() {
        if (!isPersistent())
            persist();
    }

    protected PersistenceManager getPersistenceManager() {
        return PersistenceManagerFactoryUtils.getPersistenceManager(pmf, true);
    }

    /**
     * Load an entity given its key.
     */
    protected T findByKey(Key key, Class<T> entityClass) {
        return getPersistenceManager().getObjectById(entityClass, key);
    }

    /**
     * Load an entity given its key as web-safe string.
     */
    protected T findByEncodedKey(String encodedKey, Class<T> entityClass) {
        return findByKey(KeyFactory.stringToKey(encodedKey), entityClass);
    }

    /**
     * Create keys-only query for later execution.
     * <p>
     * {@code filter} is optional.
     */
    protected Query keysOnlyQuery(Class<T> entityClass, String filter) {
        StringBuilder jdoql = new StringBuilder("select key from " + entityClass.getName());
        if (filter != null)
            jdoql.append(" where ").append(filter);

        return getPersistenceManager().newQuery(jdoql.toString());
    }

    /**
     * Datastore batch get: load multiple entities based on their keys.
     */
    @SuppressWarnings("unchecked")
    protected List<T> findAllByKeys(List<Key> keys, Class<T> entityClass) {
        if (keys.isEmpty())
            // avoid Datastore IllegalArgumentException with empty collection
            return new ArrayList<T>();

        Query q = getPersistenceManager().newQuery(entityClass, "key == :p");
        return (List<T>) q.execute(keys);
    }

    /**
     * Datastore batch get: load multiple entities based on their keys as
     * web-safe strings.
     */
    protected List<T> findAllByEncodedKeys(List<String> encodedKeys, Class<T> entityClass) {
        List<Key> keys = new ArrayList<Key>(encodedKeys.size());
        for (String encodedKey : encodedKeys)
            keys.add(KeyFactory.stringToKey(encodedKey));

        return findAllByKeys(keys, entityClass);
    }

    /**
     * Create Key-based page query for later execution.
     */
    protected KeyBasedPageQuery<T> pageQuery(int pageSize, Class<T> entityClass) {
        return pageQuery(pageSize, null, entityClass);
    }

    /**
     * Create Key-based page query for later execution.
     * <p>
     * {@code bookmark} is optional, use it for resuming previous page queries.
     */
    protected KeyBasedPageQuery<T> pageQuery(int pageSize, Key bookmark, Class<T> entityClass) {
        return new KeyBasedPageQuery<T>(pageSize, bookmark, entityClass, getPersistenceManager());
    }

    /**
     * Returns the single item of a collection or null otherwise.
     */
    protected static <P extends ModelObject<?>> P uniqueResult(List<P> items) {
        return (!items.isEmpty() && items.size() == 1) ? items.get(0) : null;
    }

    /**
     * Persist this transient entity.
     */
    public void persist() {
        getPersistenceManager().makePersistent(this);
    }

    /**
     * Delete this persistent entity.
     */
    public void delete() {
        getPersistenceManager().deletePersistent(this);
    }

    /**
     * Implementation based on Key.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getKey() == null) ? 0 : getKey().hashCode());
        return result;
    }

    /**
     * Implementation based on Key.
     */
    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        ModelObject<?> other = (ModelObject<?>) obj;
        return getKey().equals(other.getKey());
    }

}
