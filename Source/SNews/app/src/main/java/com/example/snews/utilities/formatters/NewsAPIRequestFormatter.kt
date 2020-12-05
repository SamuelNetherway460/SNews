package com.example.snews.utilities.formatters

import com.example.snews.models.Request

//https://newsapi.org/v2/top-headlines?sources=bbc-news,cnn&apiKey=d3629af64f934b1889b1fc3afb716b3c
//http://newsapi.org/v2/top-headlines?country=gb&apiKey=d3629af64f934b1889b1fc3afb716b3c
//https://newsapi.org/v2/top-headlines?category=business@category=technology&apiKey=d3629af64f934b1889b1fc3afb716b3c
//https://newsapi.org/v2/top-headlines?category=business@category=technology&apiKey=d3629af64f934b1889b1fc3afb716b3c

//TODO - Documentation
//TODO - Externalise all strings
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
        private const val DEFAULT_PAGE_SIZE = "50"
        private const val DEFAULT_REQUEST_URL = BASE_URL + TOP_HEADLINES_ENDPOINT + QUESTION_MARK +
                COUNTRY_PARAMETER_HEADER + EQUALS + DEFAULT_COUNTRY + AND + API_KEY_PARAMETER_HEADER +
                EQUALS + API_KEY

        private const val MAX_PUBLISHERS_PER_REQUEST = 20

        //TODO - Documentation
        /**
         *
         */
        fun generateRequestFromDiscoverPreferences(
            selectedCategories: ArrayList<String>,
            selectedPublishers: ArrayList<String>
        ): ArrayList<Request> {
            var requests = ArrayList<Request>()
            // If not discover preferences are selected add the default request
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

        //TODO - Documentation
        /**
         *
         *
         * @param currentRequests
         * @param selectedCategories
         * @return
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
                            API_KEY_PARAMETER_HEADER + EQUALS + API_KEY)
                )
            }
            return currentRequests
        }

        //TODO - Documentation
        /**
         *
         *
         * @param currentRequests
         * @param selectedPublishers
         * @return
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
                currentRequestURL += AND + API_KEY_PARAMETER_HEADER + EQUALS + API_KEY
                currentRequests.add(Request(currentRequestURL))
            }
            return currentRequests
        }
    }
}