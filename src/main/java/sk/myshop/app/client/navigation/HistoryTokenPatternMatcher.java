package sk.myshop.app.client.navigation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Utility class for matching History token patterns and extracting token
 * parameter names and values.
 */
public class HistoryTokenPatternMatcher {

    static final String ELEMENT_SEPARATOR = "/";
    static final String PARAMETER_PREFIX = ":";

    private final List<ElementMatcher> matcherChain = new ArrayList<ElementMatcher>();

    public HistoryTokenPatternMatcher(String pattern) {
        initMatcherChain(pattern);
    }

    private void initMatcherChain(String pattern) {
        String[] patternElements = pattern.split(ELEMENT_SEPARATOR);

        for (String element : patternElements) {
            matcherChain.add(element.startsWith(PARAMETER_PREFIX) ? new ParameterMatcher(element) : new ConstantMatcher(element));
        }
    }

    public boolean matches(String historyTokenValue) {
        return matchParameters(historyTokenValue) != null;
    }

    public Map<String, String> matchParameters(String historyTokenValue) {
        String[] tokenValueElements = historyTokenValue.split(ELEMENT_SEPARATOR);

        if (matcherChain.size() != tokenValueElements.length)
            return null;

        Map<String, String> matchResult = new HashMap<String, String>();

        for (int i = 0, c = matcherChain.size(); i < c; i++) {
            String incomingElementValue = tokenValueElements[i];
            ElementMatcher matcher = matcherChain.get(i);

            if (!matcher.matches(incomingElementValue))
                return null;

            String parameterName = matcher.parameterName();
            if (parameterName != null)
                matchResult.put(parameterName, incomingElementValue);
        }

        return matchResult;
    }

    public Set<String> getParameterNames() {
        Set<String> names = new HashSet<String>();

        for (ElementMatcher matcher : matcherChain) {
            String parameterName = matcher.parameterName();

            if (parameterName != null)
                names.add(parameterName);
        }

        return names;
    }

}

interface ElementMatcher {

    boolean matches(String incomingElementValue);

    String parameterName();

}

class ConstantMatcher implements ElementMatcher {

    private final String constantValue;

    public ConstantMatcher(String patternValue) {
        this.constantValue = patternValue;
    }

    @Override
    public boolean matches(String incomingElementValue) {
        return constantValue.equals(incomingElementValue);
    }

    @Override
    public String parameterName() {
        return null;
    }

}

class ParameterMatcher implements ElementMatcher {

    private final String parameterName;

    public ParameterMatcher(String patternValue) {
        this.parameterName = patternValue.substring(HistoryTokenPatternMatcher.PARAMETER_PREFIX.length());
    }

    @Override
    public boolean matches(String incomingElementValue) {
        return true;
    }

    @Override
    public String parameterName() {
        return parameterName;
    }

}
