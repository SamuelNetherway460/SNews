package com.example.snews.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.snews.MainActivity
import com.example.snews.utilities.Constants
import com.example.snews.utilities.formatters.NewsAPIRequestFormatter
import com.example.snews.utilities.notifications.NotificationHelper
import com.example.snews.utilities.parsers.ArticleParser
import com.koushikdutta.ion.Ion
import org.json.JSONArray
import org.json.JSONObject

//TODO - Handle when no articles are returned from a request. Make sure it doesn't bomb out.
/**
 * A service for fetching article data from the News API.
 *
 * @author Samuel Netherway
 */
class FetchArticleService : Service() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val NOTIFICATION_ONE = 101
        private const val NEW_ARTICLES_FETCHED_NOTIFICATION_TITLE = "New articles have been fetched. Check them out!"
    }

    private var notificationHelper: NotificationHelper? = null

    /**
     * Initialises the notification helper class.
     */
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "SERVICE - CREATED")
        notificationHelper = NotificationHelper(this)
    }

    /**
     * Posts a new notification to the user.
     *
     * @param id Unique identifier for the notification.
     * @param title The title of the notification.
     * @param body The main body of the notification.
     */
    fun postNotification(id: Int, title: String, body: String) {
        var notificationBuilder: NotificationCompat.Builder? = null
        when (id) {
            NOTIFICATION_ONE -> notificationBuilder = notificationHelper!!.getNotificationOne(
                title, body
            )
        }
        if (notificationBuilder != null) notificationHelper!!.notify(id, notificationBuilder)
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
     * Starts the fetch article task.
     */
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "SERVICE - STARTED")
        fetchArticles()
        Log.d(TAG, "SERVICE - COMPLETED" + intent.getBooleanExtra("appActive", true).toString())
        return START_STICKY
    }

    /**
     * Fetches the articles from the News API and writes the returned data to a file in internal storage.
     */
    private fun fetchArticles() {
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
                    postNotification(
                        NOTIFICATION_ONE,
                        NEW_ARTICLES_FETCHED_NOTIFICATION_TITLE,
                        getNotificationBody()
                    )
                    sendCategoryNotification(categoryUserPreferences)
                }
        }
    }

    /**
     * Sends notifications to the user alerting them that category specific articles have been fetched.
     */
    private fun sendCategoryNotification(selectedCategories: ArrayList<String>) {
        //TODO - Look at shared preferences for category notification selections and if they are also in the selected categories array. post a notification
    }

    /**
     * Writes json article data to internal storage.
     *
     * @param articleGroup The group of articles obtained from the News API request.
     */
    private fun addArticlesToInternalStorage(articleGroup: JSONObject) {
        var articles = ArticleParser.parseArticleGroup(articleGroup).getArticles()
        if (articles != null) articles.shuffle()
        var articleStorage = readArticleStorage()
        var jsonArticleStorage = JSONObject(articleStorage)
        if (articles != null) {
            for (article in articles) {
                jsonArticleStorage.getJSONArray(Constants.ARTICLE_STORE_JSON_ARRAY_NAME).put(article.getJSON())
            }
        }
        writeToArticleStorage(jsonArticleStorage.toString())
    }

    /**
     * Deletes the current article internal storage file and creates a new one.
     */
    private fun refreshArticleStorage() {
        deleteFile(Constants.ARTICLE_STORE_FILENAME)
        var data = JSONObject()
        data.put(Constants.ARTICLE_STORE_JSON_ARRAY_NAME, JSONArray())
        writeToArticleStorage(data.toString())
    }

    /**
     * Reads the article data from the internal storage file.
     */
    private fun readArticleStorage() : String {
        this.openFileInput(Constants.ARTICLE_STORE_FILENAME).use {
            return it.readBytes().decodeToString()
        }
    }

    /**
     * Writes string data to a pre-determined file in internal storage.
     *
     * @param string The string data to write to the file.
     */
    private fun writeToArticleStorage(string: String) {
        this.openFileOutput(Constants.ARTICLE_STORE_FILENAME, Context.MODE_PRIVATE).use {
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
        this.openFileInput(Constants.DISCOVER_PREFERENCES_FILENAME).use {
            return it.readBytes().decodeToString()
        }
    }

    /**
     * Generates the body of the notification by listing some sample publishers.
     *
     * @return A formatted string containing a list of sample publishers.
     */
    private fun getNotificationBody() : String {
        var notificationBody = ""
        var samplePublishers = ArrayList<String>()
        var articles = ArticleParser.parseArticles(
            JSONObject(readArticleStorage())
                .getJSONArray(Constants.ARTICLE_STORE_JSON_ARRAY_NAME)
        )
        // Find three sample publishers
        var currentArticle = 0
        while(samplePublishers.size <= 3 && currentArticle < articles.size) {
            var examplePublisher = articles.get(currentArticle).getSource()?.getName()
            if (examplePublisher != null && examplePublisher != "") {
                if (!samplePublishers.contains(examplePublisher)) {
                    samplePublishers.add(examplePublisher)
                }
                currentArticle++
            }
        }

        if (samplePublishers.size == 1) {
            notificationBody = "Articles available from " + samplePublishers.get(0)
        }

        if (samplePublishers.size == 2) {
            notificationBody = "Articles available from " + samplePublishers.get(0) + " and " + samplePublishers.get(
                1
            )
        }

        if (samplePublishers.size == 3) {
            notificationBody = "Articles available from " + samplePublishers.get(0) + ", " +
                    samplePublishers.get(1) + " and " + samplePublishers.get(2)
        }

        if (samplePublishers.size > 3) {
            notificationBody = "Articles available from " + samplePublishers.get(0) + ", " +
                    samplePublishers.get(1) + ", " + samplePublishers.get(2) + " and more..."
        }
        return notificationBody
    }
}