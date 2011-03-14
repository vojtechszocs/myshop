package sk.myshop.app.client.navigation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class HistoryTokenBuilderTest {

    private HistoryTokenBuilder builder;

    @Before
    public void setUp() {
        builder = new HistoryTokenBuilder("customers/:customerId/orders/:orderId");
    }

    @Test
    public void testValidateParameterNames_declaredParametersComplete() {
        builder.set("customerId", "123");
        builder.set("orderId", "456");
        builder.validateParameterNames();

        builder.set("foo", "bar");
        builder.validateParameterNames();
    }

    @Test(expected = IllegalStateException.class)
    public void testValidateParameterNames_declaredParametersIncomplete() {
        builder.set("customerId", "123");
        builder.set("foo", "bar");
        builder.validateParameterNames();
    }

    @Test
    public void testBuild() {
        builder.set("customerId", "123");
        builder.set("orderId", "456");
        String resultValue = builder.build();

        assertThat(resultValue, notNullValue());
        assertThat(resultValue, equalTo("customers/123/orders/456"));
    }

}
