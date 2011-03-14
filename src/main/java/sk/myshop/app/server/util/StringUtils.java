package sk.myshop.app.server.util;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * String utility methods.
 */
public abstract class StringUtils {

    /**
     * Locale used for string transformations.
     */
    private static final Locale LOCALE_TRANSFORM = new Locale("sk", "SK");

    public static final WordTransformCallback CB_NO_OP = new WordTransformCallback() {
        @Override
        public String transform(String word) {
            return word;
        }
    };

    public static final WordTransformCallback CB_CAPITALIZE_FIRST_LETTER = new WordTransformCallback() {
        @Override
        public String transform(String word) {
            return capitalizeFirstLetter(word);
        }
    };

    public static final WordTransformCallback CB_NORMALIZED_LOWER_CASE = new WordTransformCallback() {
        @Override
        public String transform(String word) {
            return normalizedLowerCase(word);
        }
    };

    public static String lowerCase(String value) {
        return value.toLowerCase(LOCALE_TRANSFORM);
    }

    private static String upperCase(String value) {
        return value.toUpperCase(LOCALE_TRANSFORM);
    }

    private static String normalize(String value) {
        return Normalizer.normalize(value, Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    public static String normalizedLowerCase(String value) {
        return normalize(lowerCase(value));
    }

    public static String capitalizeFirstLetter(String value) {
        return value.isEmpty() ? value : upperCase(value.substring(0, 1)) + lowerCase(value.substring(1));
    }

    /**
     * Split the given value into words and transform them.
     */
    public static List<String> transformWords(String value, WordTransformCallback callback) {
        String[] words = value.split("\\s+");
        return transformWords(Arrays.asList(words), callback);
    }

    /**
     * Transform non-empty words using the given callback.
     */
    public static List<String> transformWords(List<String> words, WordTransformCallback callback) {
        List<String> transformedWords = new ArrayList<String>(words.size());

        for (String word : words) {
            if (!word.isEmpty())
                transformedWords.add(callback.transform(word));
        }

        return transformedWords;
    }

    public static List<String> capitalizeWords(String value) {
        return transformWords(value, CB_CAPITALIZE_FIRST_LETTER);
    }

    /**
     * Join values into string using a delimiter.
     */
    public static String join(List<String> values, String delimiter) {
        StringBuilder sb = new StringBuilder();
        boolean addDelimiter = false;

        for (String value : values) {
            if (!addDelimiter)
                addDelimiter = true;
            else
                sb.append(delimiter);

            sb.append(value);
        }

        return sb.toString();
    }

    public static String join(List<String> values) {
        return join(values, " ");
    }

}
