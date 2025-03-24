package com.mbi.fakers;

import com.mbi.parameters.Parameter;
import com.mbi.parameters.SupportedParameters;

/**
 * Conducts value updating according to passed update parameter.
 * See {@link SupportedParameters} for available supported parameters.
 */
public class FakeDirector {

    /**
     * Directs value updating according to update parameter.
     *
     * @param parameter  parameter to be replaced.
     * @param fieldValue source field value.
     * @return updated field value.
     * @throws IllegalArgumentException if unsupported parameter passed.
     */
    public Object fake(final String parameter, final Object fieldValue) {
        final Parameter supportedParameter = new Parameter(parameter);

        return switch (supportedParameter.getSupportedParameter()) {
            case CALLER -> new CallerFaker().fake(String.valueOf(fieldValue), supportedParameter);
            case UID -> new UidFaker().fake(String.valueOf(fieldValue), supportedParameter);
            case DATE, CURRENT_DATE -> new DateFaker().fake(String.valueOf(fieldValue), supportedParameter);
            case DATETIME, CURRENT_DATETIME -> new DateTimeFaker().fake(String.valueOf(fieldValue), supportedParameter);
            case NUMBER -> new NumberFaker().fake(String.valueOf(fieldValue), supportedParameter);
            default -> throw new IllegalArgumentException("Unsupported parameter: " + parameter);
        };
    }
}
