package com.mbi.fakers;

/**
 * Replaces parameter with caller method name.
 */
public class CallerFaker implements Fakeable {
    @Override
    public String fake(final String sourceString, final String parameter) {
        return sourceString.replace(parameter, Thread.currentThread().getStackTrace()[5].getMethodName());
    }
}
