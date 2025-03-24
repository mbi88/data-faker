package com.mbi;

import com.mbi.parameters.SupportedParameters;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Set update parameter in json field to get it replaced with corresponding data.
 * Example:
 * {
 * "field": "Today is {$current_date}."
 * }
 * <p>
 * Returns:
 * {
 * "field": "Today is 2019-01-29."
 * }
 * <p>
 * See {@link SupportedParameters} for available supported parameters.
 */
public class JsonFaker implements Faker {

    private final ObjectFaker objectFaker = new ObjectFaker();

    /**
     * Replace update parameters with data in json.
     *
     * @param resource json object/array to be updated.
     * @param <T>      json object/array.
     * @return updated json.
     * @throws JSONException if incorrect json passed.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T fakeData(final T resource) {
        final T result;

        if (resource instanceof JSONObject) {
            result = (T) fakeJsonObjectData(new JSONObject(resource.toString()));
        } else if (resource instanceof JSONArray) {
            result = (T) fakeJsonArrayData(new JSONArray(resource.toString()));
        } else {
            throw new JSONException("Incorrect json: " + resource.toString());
        }

        return result;
    }

    /**
     * Replace update parameters with data in json object.
     *
     * @param json json to be updated.
     * @return updated json.
     */
    private JSONObject fakeJsonObjectData(final JSONObject json) {
        // Avoid updating of passed json
        final JSONObject updatedJson = new JSONObject(json.toString());
        // Update every json field
        for (String field : updatedJson.keySet()) {
            final var valueToUpdate = updatedJson.get(field);
            switch (valueToUpdate) {
                case JSONObject jsonObject -> updatedJson.put(field, fakeJsonObjectData(jsonObject));
                case JSONArray objects -> updatedJson.put(field, fakeJsonArrayData(objects));
                case String s -> updatedJson.put(field, objectFaker.fakeData(s));
                case null, default -> updatedJson.put(field, valueToUpdate);
            }
        }

        return updatedJson;
    }

    /**
     * Replace update parameters with data in json array.
     *
     * @param json json to be updated.
     * @return updated json.
     */
    private JSONArray fakeJsonArrayData(final JSONArray json) {
        // Avoid updating of passed json
        final JSONArray resultArray = new JSONArray();
        // Update every json object
        for (var valueToUpdate : json) {
            switch (valueToUpdate) {
                case JSONObject jsonObject -> resultArray.put(fakeJsonObjectData(jsonObject));
                case JSONArray objects -> resultArray.put(fakeJsonArrayData(objects));
                case null, default -> resultArray.put(objectFaker.fakeData(valueToUpdate));
            }
        }

        return resultArray;
    }
}
