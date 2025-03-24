package com.mbi.fakers;

import com.mbi.parameters.Parameter;

/**
 * Interface representing a strategy for faking values based on a specific parameter.
 * <p>
 * Implementing classes define how to generate a fake value depending on the {@link Parameter}
 * and how to inject that fake value into the provided source string.
 */
public interface Fakeable {

    /**
     * Generates a fake value and replaces the corresponding parameter inside the source string.
     *
     * @param sourceString the original string containing the parameter to be replaced
     * @param parameter    the parameter that defines how the value should be faked
     * @return the updated string with the parameter replaced by a fake value
     */
    Object fake(String sourceString, Parameter parameter);
}
