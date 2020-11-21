package com.example.snews.utilities.formatters

import android.hardware.usb.UsbEndpoint
import com.example.snews.models.Request

//https://newsapi.org/v2/top-headlines?sources=bbc-news&apiKey=d3629af64f934b1889b1fc3afb716b3c

//TODO - Documentation
//TODO - Externalise all strings
/**
 * Formats URL requests sent to the News API
 *
 * @author Samuel Netherway
 */
class NewsAPIRequestFormatter {

    private val BASE_URL = "https://newsapi.org/v2/"
    private val API_KEY_HEADER = "apiKey="

    // Request types
    private val GET = "GET"

    // Endpoints
    private val TOP_HEADLINES_ENDPOINT = "top-headlines"
    private val EVERYTHING_ENDPOINT = "everything"
    private val SOURCES_ENDPOINT = "sources"

    // Categories
    private val BUSINESS = "business"
    private val ENTERTAINMENT = "entertainment"
    private val GENERAL = "general"

    // Sorts


    private val API_KEY = "d3629af64f934b1889b1fc3afb716b3c"

    /*
    fun generateRequestsFromSearch(search: String) : Array<Request> {
        var url: String = BASE_URL + API_KEY_HEADER + API_KEY
        return arrayOf(Request(GET, url))
    }
    */

    /*
    fun identifyKeyWords(search: String) : String {

    }
     */
}