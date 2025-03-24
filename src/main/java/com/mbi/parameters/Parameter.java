package com.mbi.parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mbi.Faker.PARAMETER_END;
import static com.mbi.Faker.PARAMETER_START;

/**
 * Represents a parsed parameter from a placeholder like `{$parameter}`.
 * Handles supported types and optional arguments such as length or string flag.
 * <p>
 * Example formats:
 * <ul>
 *     <li>{@code {$uid}}</li>
 *     <li>{@code {$number;5}}</li>
 *     <li>{@code {$number;5;s}}</li>
 * </ul>
 * First part is always the supported parameter name.
 * Remaining parts are optional arguments: length, flags, etc.
 */
public final class Parameter {

    private static final int LENGTH_ARGUMENT_POSITION = 0;
    private static final int STRING_REPRESENTATION_ARGUMENT_POSITION = 1;

    private final List<String> arguments = new ArrayList<>();
    private final String fullParameter;
    private final SupportedParameters supportedParameter;

    /**
     * Parses a raw parameter string and extracts the parameter name and optional arguments.
     *
     * @param rawParameter raw parameter (e.g., "number;5;s")
     * @throws IllegalArgumentException if the parameter name is unsupported
     */
    public Parameter(final String rawParameter) {
        if (rawParameter == null || rawParameter.isBlank()) {
            throw new IllegalArgumentException("Parameter is empty or null");
        }

        final String[] parts = rawParameter.split(";");
        final String parameterName = parts[0].trim();

        if (parameterName.isEmpty()) {
            throw new IllegalArgumentException("Parameter name is missing: " + rawParameter);
        }

        this.fullParameter = PARAMETER_START + rawParameter + PARAMETER_END;

        try {
            this.supportedParameter = SupportedParameters.valueOf(parameterName.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Unsupported parameter: " + parameterName, ex);
        }

        // Store arguments if any (everything after the first part)
        if (parts.length > 1) {
            arguments.addAll(Arrays.asList(parts).subList(1, parts.length));
        }
    }

    /**
     * @return full parameter with delimiters, e.g., "{$number;5;s}"
     */
    public String getFullParameter() {
        return fullParameter;
    }

    /**
     * @return supported parameter enum (e.g., SupportedParameters.NUMBER)
     */
    public SupportedParameters getSupportedParameter() {
        return supportedParameter;
    }

    /**
     * Returns the list of arguments passed to the parameter.
     * <p>
     * For example: `{$number;5;s}` → arguments = ["5", "s"]
     *
     * @return list of arguments (maybe empty)
     */
    public List<String> getArguments() {
        return arguments;
    }

    /**
     * @return parsed length from arguments
     * @throws IllegalArgumentException if length is not a number
     */
    public int getLength() {
        try {
            return Integer.parseInt(arguments.get(LENGTH_ARGUMENT_POSITION));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new IllegalArgumentException(
                    String.format("Invalid length argument in parameter: %s", fullParameter), e
            );
        }
    }

    /**
     * Determines whether the generated fake value should be returned as a string.
     * <p>
     * This is controlled by the second argument (index 1) in the parameter string.
     * For example, if the parameter is:
     * <pre>
     * {$number;5;s}
     * </pre>
     * then:
     * <ul>
     *   <li>{@code number} — the supported parameter</li>
     *   <li>{@code 5} — the desired length of the number</li>
     *   <li>{@code s} — indicates that the resulting value should be treated as a string,
     *       even though it's numeric</li>
     * </ul>
     *
     * @return {@code true} if the result should be a string (i.e., second argument is 's'),
     * {@code false} otherwise
     */
    public boolean isStringResult() {
        // To safely access the second argument, first check that the argument list is long enough
        // This ensures we won't get an IndexOutOfBoundsException when trying to read index 1.
        return arguments.size() > STRING_REPRESENTATION_ARGUMENT_POSITION
                && "s".equalsIgnoreCase(arguments.get(STRING_REPRESENTATION_ARGUMENT_POSITION));
    }
}
