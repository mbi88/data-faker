package com.mbi.fakers;

import static com.mbi.Faker.PARAMETER_END;
import static com.mbi.Faker.PARAMETER_START;

/**
 * Conducts value updating according to passed update parameter.
 * See {@link com.mbi.fakers.SupportedParameters} for available supported parameters.
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
    public String fake(final String parameter, final String fieldValue) {
        final String updatedStr;
        final String fullParameter = String.format("%s%s%s", PARAMETER_START, parameter, PARAMETER_END);

        switch (SupportedParameters.valueOf(parameter.toUpperCase())) {
            case CALLER:
                updatedStr = new CallerFaker().fake(fieldValue, fullParameter);
                break;
            case UID:
                updatedStr = new UidFaker().fake(fieldValue, fullParameter);
                break;
            case DATE:
                updatedStr = new DateFaker().fake(fieldValue, fullParameter);
                break;
            case DATETIME:
                updatedStr = new DateTimeFaker().fake(fieldValue, fullParameter);
                break;
            case CURRENT_DATE:
                updatedStr = new DateFaker().fake(fieldValue, fullParameter);
                break;
            case CURRENT_DATETIME:
                updatedStr = new DateTimeFaker().fake(fieldValue, fullParameter);
                break;
            default:
                throw new IllegalArgumentException("Unsupported parameter: " + parameter);
        }

        return updatedStr;
    }
}
