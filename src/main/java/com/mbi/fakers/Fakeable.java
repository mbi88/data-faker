package com.mbi.fakers;

import com.mbi.parameters.Parameter;

/**
 * Replaces parameter with appropriate data.
 */
public interface Fakeable {

    /**
     * Fake data.
     *
     * @param sourceString source string.
     * @param parameter    parameter.
     * @return faked data.
     */
    Object fake(String sourceString, Parameter parameter);
}
