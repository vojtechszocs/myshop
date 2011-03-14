package sk.myshop.app.client.navigation;

import com.google.gwt.user.client.History;
import com.google.inject.Provider;

/**
 * History token parameter value provider.
 */
public abstract class HistoryTokenParameterProvider<T> implements Provider<T> {

    private final HistoryTokenPatternMatcher matcher;
    private final String parameterName;

    public HistoryTokenParameterProvider(String pattern, String parameterName) {
        this.matcher = new HistoryTokenPatternMatcher(pattern);
        this.parameterName = parameterName;
    }

    @Override
    public T get() {
        try {
            String historyToken = History.getToken();
            return matcher.matches(historyToken) ? parse(matcher.matchParameters(historyToken).get(parameterName)) : null;
        } catch (Exception e) {
            // Swallow parse exceptions
            return null;
        }
    }

    protected abstract T parse(String value) throws Exception;

    public static HistoryTokenParameterProvider<String> stringProvider(String pattern, String parameterName) {
        return new HistoryTokenParameterProvider<String>(pattern, parameterName) {
            @Override
            protected String parse(String value) {
                return value;
            }
        };
    }
    
    public static HistoryTokenParameterProvider<Long> longProvider(String pattern, String parameterName) {
        return new HistoryTokenParameterProvider<Long>(pattern, parameterName) {
            @Override
            protected Long parse(String value) {
                return Long.valueOf(value);
            }
        };
    }

}
