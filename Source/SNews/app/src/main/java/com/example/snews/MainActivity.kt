package com.example.snews

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.snews.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private var tAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation = findViewById<View>(R.id.bottom_navigation) as BottomNavigationView

        val homeFragment = HomeFragment()
        val discoverFragment = DiscoverFragment(tAuth)
        val gamesFragment = GamesFragment()
        val searchFragment = SearchFragment()
        val profileFragment = ProfileFragment(tAuth)

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

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_main, fragment)
            commit()
        }
}