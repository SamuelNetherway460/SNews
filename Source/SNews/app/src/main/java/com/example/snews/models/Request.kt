package com.example.snews.models

//TODO - Null Safety
/**
 * The Request type defines parameters for requesting content from the News API.
 *
 * @author Samuel Netherway
 * @param url The url of the request.
 * @param type The type of API request.
 */
class Request (url: String, type: String) {

    private var url: String = url
    private var type: String = type //TODO - Use enums for different required request types i.e. GET

    /**
     * @return The url for the API request.
     */
    fun getURL() : String {
        return this.url
    }

    /**
     * @return The type of API request.
     */
    fun getType() : String {
        return this.type
    }
}