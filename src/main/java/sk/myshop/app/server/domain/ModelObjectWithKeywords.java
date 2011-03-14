package sk.myshop.app.server.domain;

import static sk.myshop.app.server.util.StringUtils.CB_NORMALIZED_LOWER_CASE;
import static sk.myshop.app.server.util.StringUtils.CB_NO_OP;
import static sk.myshop.app.server.util.StringUtils.normalizedLowerCase;
import static sk.myshop.app.server.util.StringUtils.transformWords;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.api.datastore.Key;

/**
 * ModelObject with persistent keyword collection.
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class ModelObjectWithKeywords<T extends ModelObjectWithKeywords<?>> extends ModelObject<T> {

    /**
     * Keywords are stored in normalized lower case.
     */
    @Persistent
    private Set<String> keywords = new HashSet<String>();

    /**
     * @return Unmodifiable collection.
     */
    public Set<String> getKeywords() {
        return Collections.unmodifiableSet(keywords);
    }

    public void addKeyword(String keyword) {
        keywords.add(normalizedLowerCase(keyword));
    }

    public void removeKeyword(String keyword) {
        keywords.remove(normalizedLowerCase(keyword));
    }

    /**
     * Split the given value and add its words to keywords.
     */
    protected void addWordsToKeywords(String value) {
        List<String> words = transformWords(value, CB_NO_OP);

        for (String word : words)
            addKeyword(word);
    }

    /**
     * Create Key-based page query with filter on keyword collection.
     */
    protected KeyBasedPageQuery<T> keywordPageQuery(List<String> keywords, int pageSize, Key bookmark, Class<T> entityClass) {
        KeyBasedPageQuery<T> pq = pageQuery(pageSize, bookmark, entityClass);

        if (!keywords.isEmpty())
            pq.onMultipleValues("keywords", transformWords(keywords, CB_NORMALIZED_LOWER_CASE));

        return pq;
    }

}
