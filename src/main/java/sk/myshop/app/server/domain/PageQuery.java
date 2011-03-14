package sk.myshop.app.server.domain;

import static sk.myshop.app.server.util.StringUtils.lowerCase;
import static sk.myshop.app.server.util.StringUtils.normalizedLowerCase;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

/**
 * Page query using bookmark inequality filter.
 */
public abstract class PageQuery<T extends ModelObject<?>, B> {

    private final int pageSize;
    private final B bookmark;
    private final boolean pageAscending;
    private final Class<T> entityClass;
    private final PersistenceManager persistenceManager;

    private final Map<String, Object> equalityFilters = new LinkedHashMap<String, Object>();
    private final Map<String, List<?>> containsFilters = new LinkedHashMap<String, List<?>>();

    public PageQuery(int pageSize, B bookmark, boolean pageAscending, Class<T> entityClass, PersistenceManager persistenceManager) {
        this.pageSize = pageSize;
        this.bookmark = bookmark;
        this.pageAscending = pageAscending;
        this.entityClass = entityClass;
        this.persistenceManager = persistenceManager;
    }

    /**
     * Filter on field using the given value.
     */
    public PageQuery<T, B> on(String fieldName, Object value) {
        equalityFilters.put(fieldName, value);
        return this;
    }

    /**
     * Filter on field whose value is stored in lower case.
     */
    public PageQuery<T, B> onLowerCase(String fieldName, String value) {
        return on(fieldName, lowerCase(value));
    }

    /**
     * Filter on field whose value is stored in normalized lower case.
     */
    public PageQuery<T, B> onNormalizedLowerCase(String fieldName, String value) {
        return on(fieldName, normalizedLowerCase(value));
    }

    /**
     * Filter on field using {@code contains} operator.
     * <p>
     * Performance note: using {@code contains} results in multiple queries, one
     * for each item in the given collection, with results merged in the order
     * of items.
     */
    public PageQuery<T, B> onMultipleValues(String fieldName, List<?> values) {
        containsFilters.put(fieldName, values);
        return this;
    }

    /**
     * @return Bookmark extracted from the actual model object.
     */
    protected abstract B extractBookmark(T modelObject);

    /**
     * @return Name of persistent field to use in bookmark inequality filter.
     */
    protected abstract String getBookmarkFieldName();

    private String getBookmarkOperator() {
        return pageAscending ? ">=" : "<=";
    }

    private String getBookmarkOrdering() {
        return pageAscending ? "asc" : "desc";
    }

    /**
     * Initialize JDOQL filter and return filter values.
     */
    Object[] initQuery(Query query) {
        StringBuilder jdoFilter = new StringBuilder();
        List<Object> filterValues = new ArrayList<Object>();

        final String conjunction = " && ";
        int paramCounter = 0;
        boolean addConjunction = false;

        // process JDOQL equality filters and values
        for (String fieldName : equalityFilters.keySet()) {
            if (addConjunction)
                jdoFilter.append(conjunction);
            else
                addConjunction = true;

            jdoFilter.append(fieldName).append(" == :p").append(++paramCounter);
            filterValues.add(equalityFilters.get(fieldName));
        }

        // process JDOQL contains filters and values
        for (String fieldName : containsFilters.keySet()) {
            if (addConjunction)
                jdoFilter.append(conjunction);
            else
                addConjunction = true;

            jdoFilter.append(":p").append(++paramCounter).append(".contains(").append(fieldName).append(")");
            filterValues.add(containsFilters.get(fieldName));
        }

        // add bookmark inequality filter if necessary
        if (bookmark != null) {
            if (addConjunction)
                jdoFilter.append(conjunction);

            jdoFilter.append(getBookmarkFieldName()).append(" ").append(getBookmarkOperator()).append(" :p").append(++paramCounter);
            filterValues.add(bookmark);
        }

        String filter = jdoFilter.toString();
        if (!filter.isEmpty())
            query.setFilter(filter);

        return filterValues.toArray();
    }

    @SuppressWarnings("unchecked")
    public PageResult<T, B> execute() {
        Query q = persistenceManager.newQuery(entityClass);

        // apply ordering by bookmark
        q.setOrdering(getBookmarkFieldName() + " " + getBookmarkOrdering());

        // fetch N+1 items to determine next page
        q.setRange(0, pageSize + 1);

        Object[] filterValues = initQuery(q);
        List<T> items = (List<T>) q.executeWithArray(filterValues);

        boolean hasNextPage = items.size() >= pageSize + 1;
        B nextPageBookmark = hasNextPage ? extractBookmark(items.get(pageSize)) : null;
        int toIndex = hasNextPage ? pageSize : items.size();

        return new PageResult<T, B>(items.subList(0, toIndex), nextPageBookmark);
    }

}
