package com.example.snews

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.snews.fragments.*
import com.example.snews.services.FetchArticleService
import com.example.snews.utilities.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

//TODO - Full XML Check
//TODO - Make sure androidx components are being used in all XML files
/**
 * Main activity which controls navigation between application fragments (screens).
 * Activity also controls the instantiation of Firebase instances for use in across fragments.
 *
 * @author Samuel Netherway
 */
class MainActivity : AppCompatActivity() {

    private val mAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private var sharedPreferences: SharedPreferences? = null

    /**
     * Performs basic initialisation for the application.
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = this.getSharedPreferences(Constants.SHARED_PREFERENCES_FILENAME, 0)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Check if articles have been fetched before
        if (!fileExist(Constants.ARTICLE_STORE_FILENAME)) {
            startService(Intent(this, FetchArticleService::class.java))
        } else if (!fileHasArticleContent()) {
            startService(Intent(this, FetchArticleService::class.java))
        }

        // Check if the internal preferences file exits
        if (!fileExist(Constants.INTERNAL_PREFERENCES_FILENAME)) {
            createNewInternalPreferencesFile()
        }

        val bottomNavigation = findViewById<View>(R.id.bottom_navigation) as BottomNavigationView

        val homeFragment = HomeFragment()
        val discoverFragment = DiscoverFragment(mAuth, db)
        val gamesFragment = GamesFragment()
        val searchFragment = SearchFragment()
        val profileFragment = ProfileFragment(mAuth, db)

        // Default fragment
        setCurrentFragment(homeFragment)
        setFetchArticleTimes()

        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> setCurrentFragment(homeFragment)
                R.id.discover -> setCurrentFragment(discoverFragment)
                R.id.games -> setCurrentFragment(gamesFragment)
                R.id.search -> setCurrentFragment(searchFragment)
                R.id.profile -> setCurrentFragment(profileFragment)
            }
            true
        }
    }

    /**
     * Displays the selected fragment on screen.
     *
     * @param fragment The fragment to be displayed on screen.
     */
    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_main, fragment)
            commit()
        }

    /**
     * Sets up article fetching to occur at the user specified time daily.
     */
    private fun setFetchArticleTimes() {
        var hour = sharedPreferences!!.getInt(
                Constants.FETCH_ARTICLES_HOUR_FIELD_NAME, Constants.DEFAULT_FETCH_ARTICLES_HOUR)
        var minute = sharedPreferences!!.getInt(
                Constants.FETCH_ARTICLES_MINUTE_FIELD_NAME, Constants.DEFAULT_FETCH_ARTICLES_MINUTE)
        var calender = Calendar.getInstance()
        calender.set(Calendar.HOUR_OF_DAY, hour)
        calender.set(Calendar.MINUTE, minute)
        var daily = 24 * 60 * 60 * 1000
        var milliseconds = calender.timeInMillis
        // If time has already passed, add a day
        if (milliseconds < System.currentTimeMillis()) milliseconds += daily
        val intent = Intent(this, FetchArticleService::class.java)
        // FLAG to avoid creating another service if there is already one
        val pendingIntent = PendingIntent.getService(applicationContext, 1, intent, 0)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, milliseconds, pendingIntent)
    }

    /**
     * Checks whether a file exists in internal storage.
     *
     * @param filename The name of the file which is being checked for.
     * @return A boolean indicating whether the file exists in internal storage or not.
     */
    private fun fileExist(filename: String): Boolean {
        val file = baseContext.getFileStreamPath(filename)
        return file.exists()
    }

    /**
     * Checks whether a file in internal storage contains article content.
     *
     * @return A boolean indicating whether the file has article content or not.
     */
    private fun fileHasArticleContent() : Boolean {
        var jsonArticles = JSONObject(readArticleStorage())
                .getJSONArray(Constants.ARTICLE_STORE_JSON_ARRAY_NAME)
        return jsonArticles.length() != 0
    }

    /**
     * Creates a new internal preferences file and writes it to internal storage.
     */
    private fun createNewInternalPreferencesFile() {
        var internalPreferences = JSONObject()
        internalPreferences.put(Constants.INTERNAL_CATEGORIES_JSON_ARRAY_NAME, JSONArray())
        internalPreferences.put(Constants.INTERNAL_PUBLISHERS_JSON_ARRAY_NAME, JSONArray())
        internalPreferences.put(Constants.INTERNAL_SPOTLIGHT_JSON_ARRAY_NAME, JSONArray())
        internalPreferences.put(Constants.INTERNAL_HIDE_JSON_ARRAY_NAME, JSONArray())
        writeToInternalPreferencesFile(internalPreferences.toString())
    }

    /**
     * Reads the article data stored in internal storage.
     *
     * @return The article data.
     */
    private fun readArticleStorage() : String {
        this.openFileInput(Constants.ARTICLE_STORE_FILENAME).use {
            return it.readBytes().decodeToString()
        }
    }

    /**
     * Writes string data to the internal preferences file stored in internal storage.
     *
     * @param string The string data to write.
     */
    private fun writeToInternalPreferencesFile(string: String) {
        this.openFileOutput(Constants.INTERNAL_PREFERENCES_FILENAME, Context.MODE_PRIVATE).use {
            it.write(string.toByteArray())
        }
    }
}