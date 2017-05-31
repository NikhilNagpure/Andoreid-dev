package com.heniktechnology.hncore.utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class HNJsonParser {

    public static void setJSONProperty(JSONObject jsonObject, String key, Object value) {
        try {
            if (null == jsonObject) {
                return;
            }
            jsonObject.put(key, value);
        } catch (JSONException e) {
            HNLoger.debug("JSONUTIL: setJSONProperty", e.toString());
        }
    }

    public static Object getJSONProperty(JSONObject jsonObject, String key) {
        try {
            if (null == jsonObject || null == key)
                return null;
            if (jsonObject.has(key))
                return jsonObject.get(key);
        } catch (JSONException e) {
            HNLoger.debug("JSONUTIL: getJSONProperty", e.toString());
        }
        return null;
    }

    public static Object get(JSONArray jsonArray, int index) {
        try {
            return jsonArray.get(index);
        } catch (JSONException e) {
            HNLoger.debug("JSONUTIL: get", e.toString());
        }
        return null;
    }

    public static boolean isValid(JSONObject jsonObject) {
        return ((jsonObject != null) && jsonObject.length() > 0) ? true : false;
    }

    public static boolean isValid(JSONArray jsonArray) {
        return ((jsonArray != null) && jsonArray.length() > 0) ? true : false;
    }

    public static Object removeJSONProperty(JSONObject jsonObject, String key) {
        try {
            if (jsonObject.has(key)) {
                return jsonObject.remove(key);
            }
        } catch (Exception e) {
            HNLoger.debug("JSONUTIL: removeJSONProperty", e.toString());
        }
        return null;
    }

    public static JSONObject getJSONObjectFromString(String JSONString) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (JSONString != null) {
                jsonObject = new JSONObject(JSONString);
            }

        } catch (JSONException e) {
            HNLoger.debug("JSONUTIL: convertingStringToJSON", e.toString());
        }
        return jsonObject;
    }

    public static boolean hasJSONProperty(JSONObject jsonObject, String key) {
        try {
            if (jsonObject.has(key)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            HNLoger.debug("JSONUTIL: hasJSONProperty", e.toString());
        }
        return false;
    }
}
