package com.mbi.fakers;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Replaces parameter with current date.
 */
public class DateFaker implements Fakeable {

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern(DATE_PATTERN);

    @Override
    public String fake(final String sourceString, final String parameter) {
        return sourceString.replace(parameter, DATE_FORMATTER.print(new DateTime()));
    }
}
