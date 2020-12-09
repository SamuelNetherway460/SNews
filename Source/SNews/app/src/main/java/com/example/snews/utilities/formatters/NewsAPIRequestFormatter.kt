package com.example.snews.utilities.formatters

import com.example.snews.models.Request

/**
 * Formats URL requests sent to the News API
 *
 * @author Samuel Netherway
 */
class NewsAPIRequestFormatter {

    companion object {

        // Basic Operators
        private const val BASE_URL = "https://newsapi.org/v2/"
        private const val AND = "&"
        private const val EQUALS = "="
        private const val QUESTION_MARK = "?"
        private const val COMMA = ","

        // Request types
        private const val GET = "GET"

        // Endpoints
        private const val TOP_HEADLINES_ENDPOINT = "top-headlines"
        private const val EVERYTHING_ENDPOINT = "everything"
        private const val SOURCES_ENDPOINT = "sources"

        // Parameters
        private const val API_KEY_PARAMETER_HEADER = "apiKey"
        private const val COUNTRY_PARAMETER_HEADER = "country"
        private const val CATEGORY_PARAMETER_HEADER = "category"
        private const val KEYWORD_PARAMETER_HEADER = "q"
        private const val PAGE_SIZE_PARAMETER_HEADER = "pageSize"
        private const val PAGE_PARAMETER_HEADER = "page"

        // Special Values
        private const val API_KEY = "d3629af64f934b1889b1fc3afb716b3c"

        // Default values
        private const val DEFAULT_COUNTRY = "gb"
        private const val DEFAULT_PAGE_SIZE = "100"
        private const val DEFAULT_REQUEST_URL = BASE_URL + TOP_HEADLINES_ENDPOINT + QUESTION_MARK +
                COUNTRY_PARAMETER_HEADER + EQUALS + DEFAULT_COUNTRY + AND + PAGE_SIZE_PARAMETER_HEADER +
                EQUALS + DEFAULT_PAGE_SIZE + AND + API_KEY_PARAMETER_HEADER + EQUALS + API_KEY

        private const val MAX_PUBLISHERS_PER_REQUEST = 20

        /**
         * Generates a News API request based of user preferences.
         */
        fun generateRequestFromDiscoverPreferences(
            selectedCategories: ArrayList<String>,
            selectedPublishers: ArrayList<String>
        ): ArrayList<Request> {
            var requests = ArrayList<Request>()
            // If no discover preferences are selected, add the default request
            if (selectedCategories.isEmpty() && selectedPublishers.isEmpty()) requests.add(
                Request(
                    DEFAULT_REQUEST_URL
                )
            )

            if (!selectedCategories.isEmpty()) {
                requests = makeCategoryRequests(requests, selectedCategories)
            }

            if (!selectedPublishers.isEmpty()) {
                requests = makePublisherRequests(requests, selectedPublishers)
            }
            return requests
        }

        /**
         * Generates a request which will be used to query articles using a keyword.
         *
         * @param word The keyword which will be presented to the News API.
         * @return A formatted keyword request.
         */
        fun generateRequestFromKeyWord(word: String) : Request {
            return Request(BASE_URL + EVERYTHING_ENDPOINT + QUESTION_MARK +
                    KEYWORD_PARAMETER_HEADER + EQUALS + word + AND +
                    PAGE_SIZE_PARAMETER_HEADER + EQUALS + DEFAULT_PAGE_SIZE + AND +
                    API_KEY_PARAMETER_HEADER + EQUALS + API_KEY)
        }

        /**
         * Generates and adds News API category requests to the list of requests.
         *
         * @param currentRequests The current list of requests to be added to.
         * @param selectedCategories The user's category preferences.
         * @return The list of requests with the category requests added.
         */
        private fun makeCategoryRequests(
            currentRequests: ArrayList<Request>,
            selectedCategories: ArrayList<String>
        ) : ArrayList<Request> {
            for (category in selectedCategories) {
                currentRequests.add(
                    Request(BASE_URL + TOP_HEADLINES_ENDPOINT + QUESTION_MARK +
                            COUNTRY_PARAMETER_HEADER + EQUALS + DEFAULT_COUNTRY + AND +
                            CATEGORY_PARAMETER_HEADER + EQUALS + category.toLowerCase() + AND +
                            PAGE_SIZE_PARAMETER_HEADER + EQUALS + DEFAULT_PAGE_SIZE + AND +
                            API_KEY_PARAMETER_HEADER + EQUALS + API_KEY)
                )
            }
            return currentRequests
        }

        /**
         * Generates and adds News API publisher requests to the list of requests.
         *
         * @param currentRequests The current list of requests to be added to.
         * @param selectedPublishers The user's publisher preferences.
         * @return The list of requests with the publisher requests added.
         */
        private fun makePublisherRequests(
            currentRequests: ArrayList<Request>,
            selectedPublishers: ArrayList<String>
        ) : ArrayList<Request> {
            var requestGroups = selectedPublishers.chunked(MAX_PUBLISHERS_PER_REQUEST)
            for (group in requestGroups) {
                var currentRequestURL = BASE_URL + TOP_HEADLINES_ENDPOINT + QUESTION_MARK +
                        SOURCES_ENDPOINT + EQUALS
                var iterator = group.iterator()
                while (iterator.hasNext()) {
                    var next = iterator.next()
                    if (!iterator.hasNext()) {
                        currentRequestURL += next.toLowerCase().replace(' ', '-')
                    } else {
                        currentRequestURL += next.toLowerCase().replace(' ', '-') +
                                COMMA
                    }
                }
                currentRequestURL += AND + PAGE_SIZE_PARAMETER_HEADER + EQUALS + DEFAULT_PAGE_SIZE + AND +
                        API_KEY_PARAMETER_HEADER + EQUALS + API_KEY
                currentRequests.add(Request(currentRequestURL))
            }
            return currentRequests
        }
    }
}