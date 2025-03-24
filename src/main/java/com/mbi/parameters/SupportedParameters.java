package com.mbi.parameters;

/**
 * Supported parameters to be replaces with appropriate data.
 */
public enum SupportedParameters {

    /**
     * UID string parameter.
     */
    UID,

    /**
     * Caller method name.
     */
    CALLER,

    /**
     * Special marker for unsupported or unknown parameter (used in testing).
     */
    INVALID,

    /**
     * Date parameter.
     */
    DATE,

    /**
     * Datetime parameter.
     */
    DATETIME,

    /**
     * Number parameter.
     */
    NUMBER,

    /**
     * @deprecated Use {@link SupportedParameters#DATE} instead.
     * Left for backward compatibility with legacy test data.
     */
    @Deprecated
    CURRENT_DATE,

    /**
     * @deprecated Use {@link SupportedParameters#DATETIME} instead.
     * Left for backward compatibility with legacy test data.
     */
    @Deprecated
    CURRENT_DATETIME
}
