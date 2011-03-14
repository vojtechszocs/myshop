package sk.myshop.app.server.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static sk.myshop.app.server.util.StringUtils.lowerCase;
import static sk.myshop.app.server.util.StringUtils.normalizedLowerCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@RunWith(MockitoJUnitRunner.class)
public class KeyBasedPageQueryTest {

    public static final Key DUMMY_KEY = KeyFactory.stringToKey("aglub19hcHBfaWRyDgsSCEdyZWV0aW5nGAEM");

    private static class Dummy extends ModelObject<Dummy> {
        @Override
        public Key getKey() {
            return DUMMY_KEY;
        }
    }

    @Mock
    private PersistenceManager persistenceManagerMock;

    @Mock
    private Query jdoQueryMock;

    private KeyBasedPageQuery<Dummy> testQuery;

    private void initTestQuery(int pageSize, Key bookmark) {
        testQuery = new KeyBasedPageQuery<Dummy>(pageSize, bookmark, Dummy.class, persistenceManagerMock);
    }

    @Test
    public void testInitQuery_withoutBookmark() {
        String name = "John";
        Integer age = Integer.valueOf(30);

        initTestQuery(10, null);
        testQuery.on("name", name);
        testQuery.on("age", age);

        Object[] filterValues = testQuery.initQuery(jdoQueryMock);

        verify(jdoQueryMock).setFilter("name == :p1 && age == :p2");

        assertThat(filterValues.length, equalTo(2));
        assertTrue(filterValues[0].equals(name));
        assertTrue(filterValues[1].equals(age));
    }

    @Test
    public void testInitQuery_withBookmark() {
        String name = "John";
        Integer age = Integer.valueOf(30);

        initTestQuery(10, DUMMY_KEY);
        testQuery.on("name", name);
        testQuery.on("age", age);

        Object[] filterValues = testQuery.initQuery(jdoQueryMock);

        verify(jdoQueryMock).setFilter("name == :p1 && age == :p2 && key >= :p3");

        assertThat(filterValues.length, equalTo(3));
        assertTrue(filterValues[0].equals(name));
        assertTrue(filterValues[1].equals(age));
        assertTrue(filterValues[2].equals(DUMMY_KEY));
    }

    private void initTestQueryAndStubInitQuery(int pageSize, Key bookmark, final Object[] filterValues) {
        testQuery = new KeyBasedPageQuery<Dummy>(pageSize, bookmark, Dummy.class, persistenceManagerMock) {
            @Override
            Object[] initQuery(Query query) {
                return filterValues;
            }
        };
    }

    private List<Dummy> createDummyList(int size) {
        List<Dummy> dummyList = new ArrayList<Dummy>();

        for (int i = 0, c = size; i < c; i++)
            dummyList.add(new Dummy());

        return dummyList;
    }

    @Test
    public void testExecute_noMorePages() {
        String name = "John";
        Integer age = Integer.valueOf(30);
        Object[] filterValues = new Object[] { age, name };
        List<Dummy> dummyList = createDummyList(3);

        when(persistenceManagerMock.newQuery(Dummy.class)).thenReturn(jdoQueryMock);
        when(jdoQueryMock.executeWithArray(filterValues)).thenReturn(dummyList);

        initTestQueryAndStubInitQuery(10, null, filterValues);

        PageResult<Dummy, Key> result = testQuery.execute();

        verify(jdoQueryMock).setOrdering("key asc");
        verify(jdoQueryMock).setRange(0, 11);

        assertThat(result.getItems().size(), equalTo(3));
        assertThat(result.getBookmark(), equalTo(null));
        assertThat(result.hasNext(), equalTo(false));
    }

    @Test
    public void testExecute_hasMorePages() {
        String name = "John";
        Integer age = Integer.valueOf(30);
        Object[] filterValues = new Object[] { age, name };
        List<Dummy> dummyListSpy = spy(createDummyList(11));

        when(persistenceManagerMock.newQuery(Dummy.class)).thenReturn(jdoQueryMock);
        when(jdoQueryMock.executeWithArray(filterValues)).thenReturn(dummyListSpy);

        initTestQueryAndStubInitQuery(10, null, filterValues);

        PageResult<Dummy, Key> result = testQuery.execute();

        verify(jdoQueryMock).setOrdering("key asc");
        verify(jdoQueryMock).setRange(0, 11);
        verify(dummyListSpy).get(10);

        assertThat(result.getItems().size(), equalTo(10));
        assertThat(result.getBookmark(), equalTo(DUMMY_KEY));
        assertThat(result.hasNext(), equalTo(true));
    }

    @Test
    public void testFilters() {
        String name1NoOp = "John";
        String name2LowerCase = "JOHN";
        String name3NormalizedLowerCase = "Janíček";
        List<Integer> ageValues = Arrays.asList(30, 35, 40);

        initTestQuery(10, DUMMY_KEY);
        testQuery.on("name1", name1NoOp);
        testQuery.onLowerCase("name2", name2LowerCase);
        testQuery.onNormalizedLowerCase("name3", name3NormalizedLowerCase);
        testQuery.onMultipleValues("age", ageValues);

        Object[] filterValues = testQuery.initQuery(jdoQueryMock);

        verify(jdoQueryMock).setFilter("name1 == :p1 && name2 == :p2 && name3 == :p3 && :p4.contains(age) && key >= :p5");

        assertThat(filterValues.length, equalTo(5));
        assertTrue(filterValues[0].equals(name1NoOp));
        assertTrue(filterValues[1].equals(lowerCase(name2LowerCase)));
        assertTrue(filterValues[2].equals(normalizedLowerCase(name3NormalizedLowerCase)));
        assertTrue(filterValues[3].equals(ageValues));
        assertTrue(filterValues[4].equals(DUMMY_KEY));
    }

    @Test
    public void testEmptyFilters() {
        initTestQuery(10, null);

        Object[] filterValues = testQuery.initQuery(jdoQueryMock);

        verify(jdoQueryMock, never()).setFilter(anyString());

        assertThat(filterValues.length, equalTo(0));
    }

}
