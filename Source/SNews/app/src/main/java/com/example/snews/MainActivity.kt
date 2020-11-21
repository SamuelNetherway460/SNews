package com.example.snews

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.snews.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Main activity which controls navigation between application fragments (screens).
 * Activity also controls the instantiation of Firebase instances for use in across fragments.
 *
 * @author Samuel Netherway
 */
class MainActivity : AppCompatActivity() {

    private val tAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

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
        val profileFragment = ProfileFragment(tAuth)

        // Default fragment when the application is first started
        setCurrentFragment(homeFragment)

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
}