package com.example.snews.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.widget.Toast
import com.koushikdutta.ion.Ion
import android.provider.Settings
import java.io.File

//TODO - JavaDoc
//TODO - As part of service, send user notification that new articles have been successfully fetched
//TODO - Remove test alarm and replace with fetch article method from API and write to local JSON article dump
class FetchArticleService : Service() {

    val filename = "articleData"

    override fun onCreate() {
        super.onCreate()
        fetchArticles()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    //TODO - Documentation
    /**
     *
     */
    fun fetchArticles() {
        Ion.with(this)
                .load("GET", "https://newsapi.org/v2/top-headlines?sources=bbc-news&apiKey=d3629af64f934b1889b1fc3afb716b3c")
                .setHeader("user-agent", "insomnia/2020.4.1")
                .asString()
                .setCallback { ex, result ->
                    writeToFile(result)
                    Toast.makeText(applicationContext, readFromFile(), Toast.LENGTH_LONG).show()
                }
    }

    fun readFromFile() : String {
        this.openFileInput(filename).bufferedReader().useLines { lines ->
            lines.fold("") { some, text ->
                return text
            }
        }
        return "ERROR"
    }

    fun writeToFile(string: String) {
        this.openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(string.toByteArray())
        }
    }
}