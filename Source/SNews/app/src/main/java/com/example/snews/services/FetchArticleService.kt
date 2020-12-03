package com.example.snews.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.koushikdutta.ion.Ion

//TODO - JavaDoc
//TODO - As part of service, send user notification that new articles have been successfully fetched
//TODO - Remove test alarm and replace with fetch article method from API and write to local JSON article dump
/**
 * A service for fetching article data from the News API.
 *
 * @author Samuel Netherway
 */
class FetchArticleService : Service() {

    private val ARTICLE_STORE_FILENAME = "articleData"

    //TODO - Documentation
    /**
     *
     */
    override fun onCreate() {
        super.onCreate()
        fetchArticles()
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
        Ion.with(this)
                .load("GET", "http://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=d3629af64f934b1889b1fc3afb716b3c")
                .setHeader("user-agent", "insomnia/2020.4.1")
                .asString()
                .setCallback { ex, result ->
                    writeToFile(result)
                }
    }

    /**
     * Writes string data to a pre-determined file in internal storage.
     *
     * @param string The string data to write to the file.
     */
    fun writeToFile(string: String) {
        this.openFileOutput(ARTICLE_STORE_FILENAME, Context.MODE_PRIVATE).use {
            it.write(string.toByteArray())
        }
    }
}