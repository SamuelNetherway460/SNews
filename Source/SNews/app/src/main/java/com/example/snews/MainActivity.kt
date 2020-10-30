package com.example.snews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.snews.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation = findViewById<View>(R.id.bottom_navigation) as BottomNavigationView

        val homeFragment = HomeFragment()
        val discoverFragment = DiscoverFragment()
        val gamesFragment = GamesFragment()
        val searchFragment = SearchFragment()

        setCurrentFragment(homeFragment)

        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> setCurrentFragment(homeFragment)
                R.id.discover -> setCurrentFragment(discoverFragment)
                R.id.games -> setCurrentFragment(gamesFragment)
                R.id.search -> setCurrentFragment(searchFragment)
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