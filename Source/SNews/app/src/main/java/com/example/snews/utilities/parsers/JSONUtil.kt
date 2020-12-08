package com.example.snews.utilities.parsers

import org.json.JSONArray
import org.json.JSONObject

/**
 * Class for performing basic operations with JSON type files.
 *
 * @author Samuel Netherway
 */
class JSONUtil {
    companion object {
        /**
         * Converts a JSON array of JSON object to a array list of JSON objects.
         *
         * @param jsonArray The JSON array to be converted.
         * @return An array list of JSON objects.
         */
        fun jsonArrayToArrayList(jsonArray: JSONArray) : ArrayList<JSONObject> {
            var jsonArrayList = ArrayList<JSONObject>()
            for (i in 0..jsonArray.length() - 1) {
                jsonArrayList.add(jsonArray[i] as JSONObject)
            }
            return jsonArrayList
        }

        /**
         * Converts an array list of JSON objects into a JSON array.
         *
         * @param jsonArrayList The array list of json objects.
         * @return A JSON array.
         */
        fun jsonArrayListToJSONArray(jsonArrayList: ArrayList<JSONObject>) : JSONArray {
            var jsonArray = JSONArray()
            for (json in jsonArrayList) {
                jsonArray.put(json)
            }
            return jsonArray
        }
    }
}