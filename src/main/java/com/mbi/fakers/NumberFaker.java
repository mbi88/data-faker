package com.mbi.fakers;

import com.mbi.parameters.Parameter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.Random;

/**
 * Replaces the parameter with a random number.
 * Supports parameters like:
 * <ul>
 *     <li>{@code {$number}} – generates a 13-digit number by default</li>
 *     <li>{@code {$number;5}} – generates a 5-digit number</li>
 *     <li>{@code {$number;5;s}} – generates a 5-digit number as a string</li>
 * </ul>
 */
public class NumberFaker implements Fakeable {

    private static final int DEFAULT_LENGTH = 13;
    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 18;
    private static final Random RANDOM = new Random();


    /**
     * Replaces the parameter with a random number (either as `Long` or `String`, depending on argument).
     *
     * @param sourceString the original string
     * @param parameter    the parameter to replace
     * @return updated string with number injected
     */
    @Override
    public Object fake(final String sourceString, final Parameter parameter) {
        final int length = parameter.getArguments().isEmpty()
                ? DEFAULT_LENGTH
                : getValidatedLength(parameter);

        final String randomNumber = generateRandomNumber(length);
        final String replaced = sourceString.replace(parameter.getFullParameter(), randomNumber);

        // If "s" is passed or result contains non-numeric characters, return as string
        if (parameter.isStringResult() || !StringUtils.isNumeric(replaced)) {
            return replaced;
        }

        return Long.parseLong(replaced);
    }

    /**
     * Validates and returns the length of the number to be generated.
     *
     * @param parameter the parameter containing length argument
     * @return the validated length
     */
    private int getValidatedLength(final Parameter parameter) {
        final int length = parameter.getLength();

        // Validate digits count is in a supported range
        Validate.inclusiveBetween(MIN_LENGTH, MAX_LENGTH, length,
                "Number length must be between " + MIN_LENGTH + " and " + MAX_LENGTH);

        return length;
    }

    /**
     * Generates a random number of the given length (as a string).
     * Ensures that the first digit is not zero.
     *
     * @param length number of digits
     * @return random number string
     */
    private String generateRandomNumber(final int length) {
        final StringBuilder sb = new StringBuilder(length);

        // Ensure first digit is not zero
        sb.append(RANDOM.nextInt(9) + 1);

        for (int i = 1; i < length; i++) {
            sb.append(RANDOM.nextInt(10));
        }

        return sb.toString();
    }
}
