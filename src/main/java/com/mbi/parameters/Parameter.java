package com.mbi.parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.mbi.Faker.PARAMETER_END;
import static com.mbi.Faker.PARAMETER_START;

/**
 * Container for supported parameter.
 */
public class Parameter {

    private final List<String> arguments = new ArrayList<>();
    private final String fullParameter;
    private SupportedParameters supportedParameter;

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

    public String getFullParameter() {
        return fullParameter;
    }

    public SupportedParameters getSupportedParameter() {
        return supportedParameter;
    }

    public List<String> getArguments() {
        return arguments;
    }

    /**
     * @return parameter length.
     */
    public int getLength() {
        final int length;
        try {
            length = Integer.parseInt(arguments.get(0));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    String.format("Unexpected length! Expected int value: %s", getFullParameter()), e);
        }

        return length;
    }
}
