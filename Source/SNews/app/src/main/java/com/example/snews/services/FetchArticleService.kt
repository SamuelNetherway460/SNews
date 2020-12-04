package com.example.snews.services

import android.app.Service
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.snews.models.Article
import com.example.snews.utilities.formatters.NewsAPIRequestFormatter
import com.example.snews.utilities.parsers.ArticleParser
import com.google.android.material.snackbar.Snackbar
import com.koushikdutta.ion.Ion
import org.json.JSONArray
import org.json.JSONObject

//TODO - JavaDoc
//TODO - As part of service, send user notification that new articles have been successfully fetched
//TODO - Remove test alarm and replace with fetch article method from API and write to local JSON article dump
//TODO - Handle when no articles are returned from a request. Make sure it doesn't bomb out.
/**
 * A service for fetching article data from the News API.
 *
 * @author Samuel Netherway
 */
class FetchArticleService : Service() {

    private val ARTICLE_STORE_FILENAME = "articleData"
    private val DISCOVER_PREFERENCES_FILENAME = "discoverPreferences"
    private val EMPTY_STRING = ""

    //TODO - Documentation
    /**
     *
     */
    override fun onCreate() {
        super.onCreate()
        Log.d(ContentValues.TAG, "SERVICE - CREATED")
    }

    //TODO - Documentation
    /**
     *
     *
     * @param intent
     * @return
     */
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    //TODO - Documentation
    /**
     *
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(ContentValues.TAG, "SERVICE - STARTED")
        fetchArticles()
        Log.d(ContentValues.TAG, "SERVICE - COMPLETED")
        return START_STICKY
    }

    //TODO - Documentation
    /**
     *
     */
    override fun onDestroy() {
        super.onDestroy()
    }

    //TODO - Documentation
    /**
     * Fetches the articles from the News API and writes the returned data to a file in internal storage.
     */
    fun fetchArticles() {
        //TODO - Remove test preferences
        var testCategories = arrayListOf<String>("technology", "business", "general")
        var testPublishers = arrayListOf<String>("BBC News", "CNN", "MAC Rumors")

        var categoryUserPreferences = getUserCategoryPreferences()
        var publisherUserPreferences = getUserPublisherPreferences()

        var requests = NewsAPIRequestFormatter.generateRequestFromDiscoverPreferences(
                categoryUserPreferences, publisherUserPreferences
        )
        refreshArticleStorage()
        for (request in requests) {
            Log.d(TAG, request.getURL())
            Ion.with(this)
                .load("GET", request.getURL())
                .setHeader("user-agent", "insomnia/2020.4.1")
                .asString()
                .setCallback { ex, result ->
                    addArticlesToInternalStorage(JSONObject(result))
                }
        }
    }

    /**
     * Writes json article data to internal storage.
     *
     * @param articleGroup The group of articles obtained from the News API request.
     */
    private fun addArticlesToInternalStorage(articleGroup: JSONObject) {
        //TODO Shuffle articles
        var articles= ArticleParser.parseArticleGroup(articleGroup).getArticles()
        var articleStorage = readArticleStorage()
        var jsonArticleStorage = JSONObject(articleStorage)

        if (articles != null) {
            for (article in articles) {
                jsonArticleStorage.getJSONArray("data").put(article.getJSON())
            }
        }
        writeToArticleStorage(jsonArticleStorage.toString())
    }

    /**
     * Deletes the current article internal storage file and creates a new one.
     */
    private fun refreshArticleStorage() {
        deleteFile(ARTICLE_STORE_FILENAME)
        var data = JSONObject()
        data.put("data", JSONArray())
        writeToArticleStorage(data.toString())
    }

    /**
     * Reads the article data from the internal storage file.
     */
    private fun readArticleStorage() : String {
        this.openFileInput(ARTICLE_STORE_FILENAME).use {
            return it.readBytes().decodeToString()
        }
    }

    /**
     * Writes string data to a pre-determined file in internal storage.
     *
     * @param string The string data to write to the file.
     */
    private fun writeToArticleStorage(string: String) {
        this.openFileOutput(ARTICLE_STORE_FILENAME, Context.MODE_PRIVATE).use {
            it.write(string.toByteArray())
        }
    }

    /**
     * Gets the user's selected discover categories.
     *
     * @return An array list containing the user's selected discover categories.
     */
    private fun getUserCategoryPreferences() : ArrayList<String> {
        var jsonCategories = JSONObject(readDiscoverPreferences()).getJSONArray("categories") //TODO - categories final variable
        var selectedCategoriesList = ArrayList<String>()
        for (i in 0..jsonCategories.length() - 1) {
            selectedCategoriesList.add(jsonCategories.getString(i))
        }
        return selectedCategoriesList
    }

    /**
     * Gets the user's selected discover publishers.
     *
     * @return An array list containing the user's selected discover publishers.
     */
    private fun getUserPublisherPreferences() : ArrayList<String> {
        var jsonPublishers = JSONObject(readDiscoverPreferences()).getJSONArray("publishers")
        var selectedPublishersList = ArrayList<String>()
        for (i in 0..jsonPublishers.length() - 1) {
            selectedPublishersList.add(jsonPublishers.getString(i))
        }
        return selectedPublishersList
    }

    /**
     * Reads the user's discover preferences from the internal storage file.
     *
     * @return The user's discover preferences.
     */
    private fun readDiscoverPreferences() : String {
        this.openFileInput(DISCOVER_PREFERENCES_FILENAME).use {
            return it.readBytes().decodeToString()
        }
    }
}