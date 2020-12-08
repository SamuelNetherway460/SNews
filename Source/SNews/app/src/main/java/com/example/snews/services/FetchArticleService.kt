package com.example.snews.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.snews.MainActivity
import com.example.snews.R
import com.example.snews.utilities.Constants
import com.example.snews.utilities.formatters.NewsAPIRequestFormatter
import com.example.snews.utilities.notifications.NotificationHelper
import com.example.snews.utilities.parsers.ArticleParser
import com.koushikdutta.ion.Ion
import org.json.JSONArray
import org.json.JSONObject

/**
 * A service for fetching article data from the News API.
 * Writes article data to internal storage.
 * Posts appropriate notifications to user to indicate that new articles are avialable.
 *
 * @author Samuel Netherway
 */
class FetchArticleService : Service() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val NOTIFICATION_ONE = 101
        private const val CATEGORY_NOTIFICATION = 102
    }

    private var notificationHelper: NotificationHelper? = null
    private var sharedPreferences: SharedPreferences? = null

    /**
     * Initialises the notification helper class.
     */
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "SERVICE - CREATED")
        notificationHelper = NotificationHelper(this)
        sharedPreferences = this!!.getSharedPreferences(Constants.SHARED_PREFERENCES_FILENAME, 0)
    }

    /**
     * Posts a new notification to the user.
     *
     * @param id Unique identifier for the notification.
     * @param title The title of the notification.
     * @param body The main body of the notification.
     */
    private fun postNotification(id: Int, title: String, body: String) {
        var notificationBuilder: NotificationCompat.Builder? = null
        when (id) {
            NOTIFICATION_ONE -> notificationBuilder = notificationHelper!!.getNotificationOne(
                title, body
            )
            CATEGORY_NOTIFICATION -> notificationBuilder = notificationHelper!!.getCategoryNotification(
                    title, body
            )
        }
        if (notificationBuilder != null) notificationHelper!!.notify(id, notificationBuilder)
    }

    /**
     * Binds the intent to the service.
     *
     * @param intent The intent to bind.
     * @return A binder object.
     */
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    /**
     * Starts the fetch article task.
     */
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "SERVICE - STARTED")
        fetchArticles()
        Log.d(TAG, "SERVICE - COMPLETED")
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
                .load(Constants.GET, request.getURL())
                .setHeader("user-agent", "insomnia/2020.4.1")
                .asString()
                .setCallback { ex, result ->
                    addArticlesToInternalStorage(JSONObject(result))
                    postNotification(
                        NOTIFICATION_ONE,
                        resources.getString(R.string.new_articles_fetched_notification_title),
                        getNotificationBody()
                    )
                    sendCategoryNotifications(categoryUserPreferences)
                }
        }
    }

    /**
     * Sends notifications to the user alerting them that category specific articles have been fetched.
     */
    private fun sendCategoryNotifications(selectedCategories: ArrayList<String>) {
        var notificationCategories = getNotificationCategories()
        for (category in selectedCategories) {
            if (notificationCategories.contains(category.toLowerCase())) {
                postNotification(
                        CATEGORY_NOTIFICATION,
                        resources.getString(R.string.new_) + Constants.SPACE +
                                category.toLowerCase() + Constants.SPACE +
                                resources.getString(R.string._articles_fetched_notification_title),
                        getNotificationBody()
                )
            }
        }
    }

    /**
     * Looks at shared preferences and obtains all categories which notifications are switched on for.
     *
     * @return A list of categories which notifications are turned on for.
     */
    private fun getNotificationCategories() : ArrayList<String> {
        var notificationCategories = ArrayList<String>()

        // Business notifications
        if(sharedPreferences!!.getBoolean(
                resources.getString(R.string.business_category).toLowerCase(), false)) {
            notificationCategories.add(resources.getString(R.string.business_category).toLowerCase())
        }

        // Entertainment notifications
        if(sharedPreferences!!.getBoolean(
                        resources.getString(R.string.entertainment_category).toLowerCase(), false)) {
            notificationCategories.add(resources.getString(R.string.entertainment_category).toLowerCase())
        }

        // General notifications
        if(sharedPreferences!!.getBoolean(
                        resources.getString(R.string.general_category).toLowerCase(), false)) {
            notificationCategories.add(resources.getString(R.string.general_category).toLowerCase())
        }

        // Health notifications
        if(sharedPreferences!!.getBoolean(
                        resources.getString(R.string.health_category).toLowerCase(), false)) {
            notificationCategories.add(resources.getString(R.string.health_category).toLowerCase())
        }

        // Science notifications
        if(sharedPreferences!!.getBoolean(
                        resources.getString(R.string.science_category).toLowerCase(), false)) {
            notificationCategories.add(resources.getString(R.string.science_category).toLowerCase())
        }

        // Sports notifications
        if(sharedPreferences!!.getBoolean(
                        resources.getString(R.string.sports_category).toLowerCase(), false)) {
            notificationCategories.add(resources.getString(R.string.sports_category).toLowerCase())
        }

        // Technology notifications
        if(sharedPreferences!!.getBoolean(
                        resources.getString(R.string.technology_category).toLowerCase(), false)) {
            notificationCategories.add(resources.getString(R.string.technology_category).toLowerCase())
        }

        return notificationCategories
    }

    /**
     * Writes json article data to internal storage.
     *
     * @param articleGroup The group of articles obtained from the News API request.
     */
    private fun addArticlesToInternalStorage(articleGroup: JSONObject) {
        var articles = ArticleParser.parseArticleGroup(articleGroup).getArticles()

        if (articles != null) {
            articles.shuffle()
        }

        var articleStorage = readArticleStorage()
        var jsonArticleStorage = JSONObject(articleStorage)
        if (articles != null) {
            for (article in articles) {
                jsonArticleStorage.getJSONArray(Constants.ARTICLE_STORE_JSON_ARRAY_NAME)
                        .put(article.getJSON())
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
     * Writes string data to the article file store in internal storage.
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
        var jsonCategories = JSONObject(readInternalPreferences()).getJSONArray(
                Constants.INTERNAL_CATEGORIES_JSON_ARRAY_NAME)
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
        var jsonPublishers = JSONObject(readInternalPreferences()).getJSONArray(
                Constants.INTERNAL_PUBLISHERS_JSON_ARRAY_NAME)
        var selectedPublishersList = ArrayList<String>()
        for (i in 0..jsonPublishers.length() - 1) {
            selectedPublishersList.add(jsonPublishers.getString(i))
        }
        return selectedPublishersList
    }

    /**
     * Reads the user's preferences from the internal storage file.
     *
     * @return The user's preferences.
     */
    private fun readInternalPreferences() : String {
        this.openFileInput(Constants.INTERNAL_PREFERENCES_FILENAME).use {
            return it.readBytes().decodeToString()
        }
    }

    /**
     * Generates the body of the notification by listing some sample publishers.
     *
     * @return A formatted string containing a list of sample publishers.
     */
    private fun getNotificationBody() : String {
        var notificationBody = Constants.EMPTY_STRING
        var samplePublishers = ArrayList<String>()
        var articles = ArticleParser.parseArticles(
            JSONObject(readArticleStorage())
                .getJSONArray(Constants.ARTICLE_STORE_JSON_ARRAY_NAME)
        )
        // Find three sample publishers
        var currentArticle = 0
        while(samplePublishers.size <= 3 && currentArticle < articles.size) {
            var examplePublisher = articles.get(currentArticle).getSource()?.getName()
            if (examplePublisher != null && examplePublisher != Constants.EMPTY_STRING) {
                if (!samplePublishers.contains(examplePublisher)) {
                    samplePublishers.add(examplePublisher)
                }
                currentArticle++
            }
        }

        if (samplePublishers.size == 1) {
            notificationBody = resources.getString(R.string.articles_available_from) +
                    samplePublishers.get(0)
        }

        if (samplePublishers.size == 2) {
            notificationBody = resources.getString(R.string.articles_available_from) +
                    samplePublishers.get(0) + resources.getString(R.string.and) +
                    samplePublishers.get(
                1
            )
        }

        if (samplePublishers.size == 3) {
            notificationBody = resources.getString(R.string.articles_available_from) +
                    samplePublishers.get(0) + resources.getString(R.string.comma_space) +
                    samplePublishers.get(1) + resources.getString(R.string.and) +
                    samplePublishers.get(2)
        }

        if (samplePublishers.size > 3) {
            notificationBody = resources.getString(R.string.articles_available_from) +
                    samplePublishers.get(0) + resources.getString(R.string.comma_space) +
                    samplePublishers.get(1) + resources.getString(R.string.comma_space) +
                    samplePublishers.get(2) + resources.getString(R.string.and_more)
        }
        return notificationBody
    }
}