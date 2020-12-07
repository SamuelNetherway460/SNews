package com.example.snews.models

/**
 * The Request type defines parameters for requesting content from the News API.
 *
 * @property url The url of the request.
 * @author Samuel Netherway
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