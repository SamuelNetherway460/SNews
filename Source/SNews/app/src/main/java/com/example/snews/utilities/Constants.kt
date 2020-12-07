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
        const val SPACE = " "

        // Fragment Navigation Tags
        const val PROFILE_FRAGMENT_TAG = "ProfileFragment"
        const val SIGN_IN_REGISTER_TAG = "SignInRegisterFragment"

        // Shared Preferences
        const val SHARED_PREFERENCES_FILENAME = "sharedPreferences"
        const val FETCH_ARTICLES_HOUR_FIELD_NAME = "fetchArticlesHour"
        const val FETCH_ARTICLES_MINUTE_FIELD_NAME = "fetchArticlesMinute"
        const val DEFAULT_FETCH_ARTICLES_HOUR = 13
        const val DEFAULT_FETCH_ARTICLES_MINUTE = 32

        // Internal Storage
        const val INTERNAL_PREFERENCES_FILENAME = "internalPreferences"
        const val ARTICLE_STORE_FILENAME = "articleData"
        const val ARTICLE_STORE_JSON_ARRAY_NAME = "data"
        const val INTERNAL_CATEGORIES_JSON_ARRAY_NAME = "categories"
        const val INTERNAL_PUBLISHERS_JSON_ARRAY_NAME = "publishers"
        const val INTERNAL_SPOTLIGHT_JSON_ARRAY_NAME = "spotlight"
        const val INTERNAL_HIDE_JSON_ARRAY_NAME = "hide"
        const val INTERNAL_WORD_JSON_NAME = "word"
        const val INTERNAL_ENABLED_JSON_NAME = "enabled"

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

        // Firestore
        const val FIRESTORE_USERS_COLLECTION_PATH = "users"
        const val FIRESTORE_FIRST_NAME_FIELD = "first_name"
        const val FIRESTORE_LAST_NAME_FIELD = "last_name"
        const val FIRESTORE_CATEGORIES_FIELD = "categories"
        const val FIRESTORE_PUBLISHERS_FIELD = "publishers"
        const val FIRESTORE_SPOTLIGHT_FIELD = "spotlight"
        const val FIRESTORE_HIDE_FIELD = "hide"

        // Article fetching
        const val GET = "GET"
    }
}