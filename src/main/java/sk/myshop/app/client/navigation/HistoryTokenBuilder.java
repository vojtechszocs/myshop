package sk.myshop.app.client.navigation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Utility class for building History token values from patterns.
 */
public class HistoryTokenBuilder {

    private final String pattern;
    private final Map<String, String> parameters = new HashMap<String, String>();

    public HistoryTokenBuilder(String pattern) {
        this.pattern = pattern;
    }

    public void set(String name, String value) {
        parameters.put(name, value);
    }

    void validateParameterNames() {
        Set<String> patternParameterNames = new HistoryTokenPatternMatcher(pattern).getParameterNames();
        Set<String> declaredParameterNames = parameters.keySet();

        for (String parameterName : patternParameterNames) {
            if (!declaredParameterNames.contains(parameterName))
                throw new IllegalStateException("Undefined parameter value for " + parameterName);
        }
    }

    public String build() {
        validateParameterNames();

        final String elementSeparator = HistoryTokenPatternMatcher.ELEMENT_SEPARATOR;
        final String parameterPrefix = HistoryTokenPatternMatcher.PARAMETER_PREFIX;
        StringBuilder resultValue = new StringBuilder();

        String[] patternElements = pattern.split(elementSeparator);
        boolean appendSeparator = false;

        for (String element : patternElements) {
            if (!appendSeparator)
                appendSeparator = true;
            else
                resultValue.append(elementSeparator);

            if (element.startsWith(parameterPrefix)) {
                String parameterName = element.substring(parameterPrefix.length());
                resultValue.append(parameters.get(parameterName));
            } else {
                resultValue.append(element);
            }
        }

        return resultValue.toString();
    }

}
