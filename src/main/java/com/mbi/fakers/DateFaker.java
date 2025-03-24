package com.mbi.fakers;

import com.mbi.parameters.Parameter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Replaces a parameter with the current date in ISO format (yyyy-MM-dd).
 * <p>
 * Example:
 * <pre>
 * Input:  "Today is {$date}."
 * Output: "Today is 2024-03-25."
 * </pre>
 */
public class DateFaker implements Fakeable {

    /**
     * Default pattern for ISO-style date.
     */
    private static final String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * Formatter used to produce the date string.
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern(DATE_PATTERN);

    /**
     * Replaces a parameter inside a string with the current date.
     *
     * @param sourceString the original string containing the parameter
     * @param parameter the parameter metadata
     * @return the string with the parameter replaced by current date
     */
    @Override
    public String fake(final String sourceString, final Parameter parameter) {
        final var today = FORMATTER.print(DateTime.now());
        return sourceString.replace(parameter.getFullParameter(), today);
    }
}
