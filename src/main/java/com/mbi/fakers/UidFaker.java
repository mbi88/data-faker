package com.mbi.fakers;

import com.mbi.parameters.Parameter;

import java.util.UUID;

/**
 * Replaces a parameter with a randomly generated UUID.
 * <p>
 * Optionally, you can provide a length argument to truncate the UUID.
 * Example:
 * <pre>
 * Input:  "User ID: {$uid}"
 * Output: "User ID: 2b1198cc-5d10-4d4e-8f43-1e0f9e2bfaee"
 *
 * Input with argument:  "User ID: {$uid;8}"
 * Output:               "User ID: 2b1198cc"
 * </pre>
 */
public class UidFaker implements Fakeable {

    /**
     * Replaces a {@code {$uid}} parameter in the string with a UUID (optionally truncated).
     *
     * @param sourceString the original string containing the placeholder
     * @param parameter    parsed parameter metadata, including optional length
     * @return the string with the parameter replaced by a UUID (or shortened UUID)
     */
    @Override
    public String fake(final String sourceString, final Parameter parameter) {
        final var uuid = UUID.randomUUID().toString();
        final var resultUuid = parameter.getArguments().isEmpty()
                ? uuid
                : uuid.substring(0, parameter.getLength());

        return sourceString.replace(parameter.getFullParameter(), resultUuid);
    }
}
