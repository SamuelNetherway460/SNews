package com.example.snews

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.snews.fragments.*
import com.example.snews.services.FetchArticleService
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

//TODO - Check edge cases, if articles have never been fetched before, etc. No articles returned from api, etc
//TODO - Full XML Check
//TODO - Make sure androidx components are being used in all XML files
//TODO - Rename images, remove not required ones
//TODO - Enable offline firestore, cache etc
//TODO - Make all relevant methods private
//TODO - Sort out final variables
/**
 * Main activity which controls navigation between application fragments (screens).
 * Activity also controls the instantiation of Firebase instances for use in across fragments.
 *
 * @author Samuel Netherway
 */
class MainActivity : AppCompatActivity() {

    private val ARTICLE_STORE_FILENAME = "articleData"
    private val DISCOVER_PREFERENCES_FILENAME = "discoverPreferences"
    private val mAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val hour = 18 //TODO - Move to shared preferences
    private val minute = 12 //TODO - Move to shared preferences

    //TODO - Documentation
    /**
     *
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check if articles have been fetched before
        if (!fileExist(ARTICLE_STORE_FILENAME)) {
            startService(Intent(this, FetchArticleService::class.java))
        } else if (!fileHasArticleContent()) {
            startService(Intent(this, FetchArticleService::class.java))
        }

        // Check if the discover preferences file exits
        if (!fileExist(DISCOVER_PREFERENCES_FILENAME)) {
            createNewDiscoverPreferencesFile()
        }

        val bottomNavigation = findViewById<View>(R.id.bottom_navigation) as BottomNavigationView

        val homeFragment = HomeFragment()
        val discoverFragment = DiscoverFragment(mAuth, db)
        val gamesFragment = GamesFragment()
        val searchFragment = SearchFragment()
        val profileFragment = ProfileFragment(mAuth, db)

        // Default fragment
        setCurrentFragment(homeFragment)
        setAlarm()

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

    //TODO - Change, based off shared preferences
    //TODO - Documentation
    /**
     *
     */
    private fun setAlarm() {
        var calender = Calendar.getInstance()
        calender.set(Calendar.HOUR_OF_DAY, hour)
        calender.set(Calendar.MINUTE, minute)
        var daily = 24 * 60 * 60 * 1000
        var milliseconds = calender.timeInMillis //TODO - Use better name for variable
        // If time has already passed, add a day
        //if (milliseconds < System.currentTimeMillis()) milliseconds += daily
        val intent = Intent(this, FetchArticleService::class.java)
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
        var jsonArticles = JSONObject(readArticleStorage()).getJSONArray("data")
        return jsonArticles.length() != 0
    }

    /**
     * Creates a new discover preferences file and writes it to internal storage.
     */
    private fun createNewDiscoverPreferencesFile() {
        var discoverPreferences = JSONObject()
        discoverPreferences.put("categories", JSONArray())
        discoverPreferences.put("publishers", JSONArray())
        writeToDiscoverPreferencesFile(discoverPreferences.toString())
    }

    /**
     * Reads the article data stored in internal storage.
     *
     * @return The article data.
     */
    private fun readArticleStorage() : String {
        this.openFileInput(ARTICLE_STORE_FILENAME).use {
            return it.readBytes().decodeToString()
        }
    }

    /**
     * Writes string data to the discover preferences file stored in internal storage.
     *
     * @param string The string data to write.
     */
    private fun writeToDiscoverPreferencesFile(string: String) {
        this.openFileOutput(DISCOVER_PREFERENCES_FILENAME, Context.MODE_PRIVATE).use {
            it.write(string.toByteArray())
        }
    }
}