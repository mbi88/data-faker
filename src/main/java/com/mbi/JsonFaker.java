package com.mbi;

import com.mbi.fakers.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
 * See {@link com.mbi.fakers.SupportedParameters} for available supported parameters.
 */
public class JsonFaker implements Faker {

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
        final FakeDirector fakeDirector = new FakeDirector();
        // Avoid updating of passed json
        final JSONObject updatedJson = new JSONObject(json.toString());
        // Update every json field
        for (String field : updatedJson.keySet()) {
            final String value = updatedJson.get(field).toString();

            final List<String> parameters = getParams(value);
            String updatedValue = value;
            // Update only if parameters exist
            if (!parameters.isEmpty()) {
                // Support several parameters in a field
                for (String par : parameters) {
                    // Replace update parameter with appropriate data
                    updatedValue = fakeDirector.fake(par, updatedValue);
                    // Put updated data to json
                    updatedJson.put(field, getPutObject(updatedValue));
                }
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
        final JSONArray resultArray = new JSONArray();
        for (Object o : json) {
            resultArray.put(fakeData((JSONObject) o));
        }

        return resultArray;
    }

    /**
     * Returns json object/array if updated json field value is json or string instead.
     *
     * @param updatedValue json field value.
     * @return json object/array or string.
     */

    private Object getPutObject(final String updatedValue) {
        final Object result;
        if (Objects.equals(updatedValue.charAt(0), '{')) {
            result = new JSONObject(updatedValue);
        } else if (Objects.equals(updatedValue.charAt(0), '[')) {
            result = new JSONArray(updatedValue);
        } else {
            result = updatedValue;
        }

        return result;
    }

    /**
     * Get parameters from json field.
     *
     * @param jsonFieldValue json field value.
     * @return list of parameters if exist or empty list.
     */
    private List<String> getParams(final String jsonFieldValue) {
        final List<String> list = new ArrayList<>();
        String s = jsonFieldValue;

        while (s.contains(PARAMETER_START)) {
            // Remove the sign of the beginning of the parameter and all before
            s = s.substring(s.indexOf(PARAMETER_START) + 2);
            // Remove the sign of the end of the parameter and all after
            final String param = s.substring(0, s.indexOf(PARAMETER_END));
            // Store parameter
            list.add(param);
        }

        return list;
    }
}
