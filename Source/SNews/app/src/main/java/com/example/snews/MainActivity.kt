package com.example.snews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.snews.fragments.*
import com.example.snews.parsers.ArticleParser
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.koushikdutta.ion.Ion
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //getArticle()

        val bottomNavigation = findViewById<View>(R.id.bottom_navigation) as BottomNavigationView

        val homeFragment = HomeFragment()
        val discoverFragment = DiscoverFragment()
        val gamesFragment = GamesFragment()
        val searchFragment = SearchFragment()
        val profileFragment = ProfileFragment()

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

    /*
    fun getArticle() {
        Ion.with(this)
            .load("GET", "https://newsapi.org/v2/top-headlines?sources=bbc-news&pageSize=1&apiKey=d3629af64f934b1889b1fc3afb716b3c")
            .setHeader("user-agent", "insomnia/2020.4.1")
            .asString()
            .setCallback { ex, result ->
                test(result)
            }
    }

    fun test(data: String) {

        var articleGroup = ArticleParser.parseArticleGroup(JSONObject(data))
        var status = articleGroup.getStatus()
        var title = articleGroup.getArticles()?.get(0)?.getTitle()

        println("*****************************HERE*****************************")
        println("*****************************HERE*****************************")
        println("*****************************HERE*****************************")
        println("*****************************HERE*****************************")
        println("Status: " + status)
        println("Content: " + title)
    }
    */
}