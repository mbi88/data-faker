package com.mbi;

import com.mbi.parameters.SupportedParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Set update parameter in string to get it replaced with corresponding data.
 * Example:
 * String field = "Today is {$current_date}.";
 * <p>
 * Returns:
 * String field = "Today is 2019-01-29.";
 * <p>
 * See {@link SupportedParameters} for available supported parameters.
 */
public class ObjectFaker implements Faker {

    // Regex to extract all parameters like {$...}
    private static final Pattern PARAMETER_PATTERN = Pattern.compile("\\{\\$([^{}\\s]+?)}");

    /**
     * Replaces all parameter placeholders in the input string with fake data.
     *
     * @param resource string to be updated
     * @param <T>      the object type (typically String)
     * @return the object with replaced placeholder values
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T fakeData(final T resource) {
        T result = resource;

        if (resource instanceof String string) {
            // Extract parameters like current_date, uid, etc.
            final List<String> parameters = extractParameters(string);

            // If there are no parameters, return the original string as-is
            if (!parameters.isEmpty()) {
                // Replace each parameter with the corresponding fake value
                Object updated = string;
                for (var param : parameters) {
                    updated = FAKE_DIRECTOR.fake(param, updated);
                }
                result = (T) updated;
            }
        }

        return result;
    }

    /**
     * Extracts all parameter names from the given string.
     * Parameters must be in the format: {$parameter}.
     * <p>
     * Example:
     * Input:  "Today is {$date} and your ID is {$uid}"
     * Output: ["date", "uid"]
     * <p>
     * If any parameter is not properly closed with '}', an IllegalArgumentException is thrown.
     *
     * @param value the string to search for parameters
     * @return a list of extracted parameter names
     * @throws IllegalArgumentException if a malformed parameter is found
     */
    private List<String> extractParameters(final String value) {
        final var params = new ArrayList<String>();
        final var matcher = PARAMETER_PATTERN.matcher(value);
        int lastMatchEnd = 0;

        while (matcher.find()) {
            // Check for unclosed '{$' before the current match
            final var between = value.substring(lastMatchEnd, matcher.start());
            if (between.contains(PARAMETER_START)) {
                throw new IllegalArgumentException("Incorrect parameter: " + value);
            }

            // Add the parameter name found inside {$...}
            params.add(matcher.group(1));
            lastMatchEnd = matcher.end();
        }

        // After the last match, check if there is still an unmatched '{$'
        if (value.substring(lastMatchEnd).contains(PARAMETER_START)) {
            throw new IllegalArgumentException("Incorrect parameter: " + value);
        }

        return params;
    }
}
