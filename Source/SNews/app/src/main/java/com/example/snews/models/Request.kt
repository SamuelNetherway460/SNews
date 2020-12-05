package com.example.snews.models

//TODO - Null Safety
/**
 * The Request type defines parameters for requesting content from the News API.
 *
 * @author Samuel Netherway
 * @property url The url of the request.
 */
class Request (url: String) {

    private var url: String = url

    /**
     * @return The url for the API request.
     */
    fun getURL() : String {
        return this.url
    }
}