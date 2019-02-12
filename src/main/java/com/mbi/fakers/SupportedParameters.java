package com.mbi.fakers;

/**
 * Supported parameters to be replaces with appropriate data.
 */
public enum SupportedParameters {

    UID,
    CALLER,
    INVALID,
    DATE,
    DATETIME,

    /**
     * @deprecated Use {@link com.mbi.fakers.SupportedParameters#DATE} instead.
     */
    @Deprecated
    CURRENT_DATE,

    /**
     * @deprecated Use {@link com.mbi.fakers.SupportedParameters#DATETIME} instead.
     */
    @Deprecated
    CURRENT_DATETIME
}
