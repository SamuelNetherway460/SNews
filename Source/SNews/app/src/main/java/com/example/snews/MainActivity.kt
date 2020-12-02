package com.example.snews

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.snews.fragments.*
import com.example.snews.services.FetchArticleService
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


//TODO - Full XML Check
//TODO - Make sure androidx components are being used in all XML files
//TODO - Rename images, remove not required ones
/**
 * Main activity which controls navigation between application fragments (screens).
 * Activity also controls the instantiation of Firebase instances for use in across fragments.
 *
 * @author Samuel Netherway
 */
class MainActivity : AppCompatActivity() {

    private val tAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val hour = 21
    private val minute = 55

    //TODO - Documentation
    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation = findViewById<View>(R.id.bottom_navigation) as BottomNavigationView

        val homeFragment = HomeFragment()
        val discoverFragment = DiscoverFragment(tAuth, db)
        val gamesFragment = GamesFragment()
        val searchFragment = SearchFragment()
        val profileFragment = ProfileFragment(tAuth, db)

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

    //TODO - Change
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
}