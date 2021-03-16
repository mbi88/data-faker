package com.mbi.fakers;

import com.mbi.parameters.Parameter;

/**
 * Replaces parameter with uid.
 */
public class UidFaker implements Fakeable {
    @Override
    public String fake(final String sourceString, final Parameter parameter) {
        final var uid = java.util.UUID.randomUUID().toString();
        final var resultUid = parameter.getArguments().isEmpty() ? uid : uid.substring(0, parameter.getLength());

        return sourceString.replace(parameter.getFullParameter(), resultUid);
    }
}
