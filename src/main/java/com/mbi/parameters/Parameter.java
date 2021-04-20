package com.mbi.parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.mbi.Faker.PARAMETER_END;
import static com.mbi.Faker.PARAMETER_START;

/**
 * Container for supported parameter.
 * 1st argument - expected length;
 * 2nd argument - expected format (should be 's' if string expected)
 */
public class Parameter {

    private static final int LENGTH_ARGUMENT_POSITION = 0;
    private static final int STRING_REPRESENTATION_ARGUMENT_POSITION = 1;
    private final List<String> arguments = new ArrayList<>();
    private final String fullParameter;
    private SupportedParameters supportedParameter;

    /**
     * @param rawParameter parameter to be replaced.
     */
    public Parameter(final String rawParameter) {
        this.fullParameter = String.format("%s%s%s", PARAMETER_START, rawParameter, PARAMETER_END);

        Stream.of(rawParameter)
                // Split arguments
                .forEach(s -> {
                    final String[] args = s.split(";");
                    this.supportedParameter = SupportedParameters.valueOf(args[0].toUpperCase());
                    arguments.addAll(Arrays.asList(args).subList(1, args.length));
                });
    }

    /**
     * @return parameter raw value.
     */
    public String getFullParameter() {
        return fullParameter;
    }

    /**
     * @return supported parameters.
     */
    public SupportedParameters getSupportedParameter() {
        return supportedParameter;
    }

    /**
     * @return parameter arguments list.
     */
    public List<String> getArguments() {
        return arguments;
    }

    /**
     * @return parameter length.
     */
    public int getLength() {
        final int length;
        try {
            length = Integer.parseInt(arguments.get(LENGTH_ARGUMENT_POSITION));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    String.format("Unexpected length! Expected int value: %s", getFullParameter()), e);
        }

        return length;
    }

    /**
     * @return if faked data should be represented as string
     */
    public boolean isStringResult() {
        if (arguments.size() < STRING_REPRESENTATION_ARGUMENT_POSITION + 1) {
            return false;
        }

        return "s".equalsIgnoreCase(arguments.get(STRING_REPRESENTATION_ARGUMENT_POSITION));
    }
}
