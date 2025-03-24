package com.mbi.fakers;

import com.mbi.parameters.Parameter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Replaces the parameter with the current datetime.
 * Example:
 * Input:  "Generated at {$datetime}"
 * Output: "Generated at 2024-03-24T19:42:00"
 */
public class DateTimeFaker implements Fakeable {

    /**
     * ISO-8601-like datetime format: yyyy-MM-dd'T'HH:mm:ss.
     */
    private static final String DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * Formatter used to format the current datetime.
     */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern(DATETIME_PATTERN);

    /**
     * Replaces the parameter with the current datetime string.
     *
     * @param sourceString the string that contains the parameter
     * @param parameter    the parameter to be replaced
     * @return the string with the parameter replaced by the current datetime
     */
    @Override
    public String fake(final String sourceString, final Parameter parameter) {
        final var now = DATE_TIME_FORMATTER.print(DateTime.now());
        return sourceString.replace(parameter.getFullParameter(), now);
    }
}
