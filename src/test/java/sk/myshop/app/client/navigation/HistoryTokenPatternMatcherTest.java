package sk.myshop.app.client.navigation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class HistoryTokenPatternMatcherTest {

    private HistoryTokenPatternMatcher matcher;

    @Before
    public void setUp() {
        matcher = new HistoryTokenPatternMatcher("customers/:customerId/orders/:orderId");
    }

    @Test
    public void testMatches() {
        assertThat(matcher.matches("customers/123"), is(false));
        assertThat(matcher.matches("customers/123/orders/456"), is(true));
        assertThat(matcher.matches("customers/123/orders/456/extra"), is(false));
        assertThat(matcher.matches("extra/customers/123/orders/456"), is(false));
    }

    @Test
    public void testMatchParameters() {
        Map<String, String> matchResult = matcher.matchParameters("customers/123/orders/456");

        assertThat(matchResult, notNullValue());
        assertThat(matchResult.size(), is(2));

        String customerId = matchResult.get("customerId");
        String orderId = matchResult.get("orderId");

        assertThat(customerId, notNullValue());
        assertThat(customerId, equalTo("123"));
        assertThat(orderId, notNullValue());
        assertThat(orderId, equalTo("456"));
    }

    @Test
    public void testGetParameterNames() {
        Set<String> parameterNames = matcher.getParameterNames();

        assertThat(parameterNames, notNullValue());
        assertThat(parameterNames.size(), is(2));

        assertThat(parameterNames.contains("customerId"), is(true));
        assertThat(parameterNames.contains("orderId"), is(true));
    }

    @Test
    public void testSimple() throws Exception {
        matcher = new HistoryTokenPatternMatcher("shops");

        assertThat(matcher.matches("shops"), is(true));
        assertThat(matcher.matches("shop"), is(false));
        assertThat(matcher.matches("shops/"), is(true));
        assertThat(matcher.matches("shops/123"), is(false));
    }

}
