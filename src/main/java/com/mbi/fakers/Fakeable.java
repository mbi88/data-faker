package com.mbi.fakers;

/**
 * Replaces parameter with appropriate data.
 */
public interface Fakeable {
    String fake(String sourceString, String parameter);
}
