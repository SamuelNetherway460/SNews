package com.example.snews.utilities

/**
 * A class to store global values used throughout the entire application.
 *
 * @author Samuel Netherway
 */
class Constants {

    /**
     * Companion object containing global values used throughout the entire application.
     */
    companion object {
        // General
        const val EMPTY_STRING = ""

        // Internal storage file names
        const val DISCOVER_PREFERENCES_FILENAME = "discoverPreferences"
        const val ARTICLE_STORE_FILENAME = "articleData"

        // Article Group data defaults
        const val NO_STATUS = "No Status"
        const val NULL_RESULTS = -1

        // Article data defaults
        const val NO_AUTHOR = "No Author"
        const val NO_TITLE = "No Title"
        const val NO_DESCRIPTION = "No Description"
        const val NO_URL = "No URL"
        const val NO_IMAGE_URL = "No Image URL"
        const val NO_PUBLISH_DATE = "No Publish Date"
        const val NO_CONTENT = "No Content"

        // Source data defaults
        const val NO_ID = "No ID"
        const val NO_NAME = "No Name"
    }
}