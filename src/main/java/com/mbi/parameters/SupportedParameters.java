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
     * Invalid parameter for testing purpose.
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
     */
    @Deprecated
    CURRENT_DATE,

    /**
     * @deprecated Use {@link SupportedParameters#DATETIME} instead.
     */
    @Deprecated
    CURRENT_DATETIME
}
