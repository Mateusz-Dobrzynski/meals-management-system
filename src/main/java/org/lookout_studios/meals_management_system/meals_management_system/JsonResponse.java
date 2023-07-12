package org.lookout_studios.meals_management_system.meals_management_system;

import org.json.JSONObject;

/*
 * This class can be used to generate JSON API responses
 */
public class JsonResponse {
    private JSONObject responseJson;

    public JsonResponse(Object... keyValuePairs) {
        responseJson = new JSONObject();
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            String key = String.valueOf(keyValuePairs[i]);
            Object value = keyValuePairs[i + 1];
            responseJson.put(key, value);
        }
    }

<<<<<<< HEAD
    /**
     * @return JSON response as string
=======
    
    /** 
     * @return String as a JSON 
>>>>>>> parent of d57f3bc ([19] implement JSON responses and HTTP statuses)
     */
    public String getResponse() {
        return responseJson.toString();
    }
<<<<<<< HEAD

    /**
     * Adds a single property to the JSON object
     * 
     * @param key   Key of the JSON property
     * @param value Value of the JSON property
     */
    public void addProperty(Object key, Object value) {
        try {
            responseJson.put(key.toString(), value.toString());
        } catch (JSONException exception) {
            throw exception;
        }
    }

    /**
     * Adds a property with a response status and a status code
     * 
     * @param status API status as stated in the ResponseStatus
     */
    public void addStatus(ResponseStatus status) {
        this.addProperty("message", status.getStatusName());
        this.addProperty("status", status.getCode());
    }
=======
>>>>>>> parent of d57f3bc ([19] implement JSON responses and HTTP statuses)
}

