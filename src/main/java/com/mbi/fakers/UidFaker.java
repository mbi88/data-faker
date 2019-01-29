package com.mbi.fakers;

/**
 * Replaces parameter with uid.
 */
public class UidFaker implements Fakeable {
    @Override
    public String fake(final String sourceString, final String parameter) {
        return sourceString.replace(parameter, java.util.UUID.randomUUID().toString());
    }
}
