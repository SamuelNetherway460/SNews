package com.example.snews.fragments

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.snews.R
import com.example.snews.models.Article
import com.example.snews.utilities.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Fragment responsible for displaying news articles.
 *
 * @property article The article to be displayed
 * @author Samuel Netherway
 */
class ArticleViewerFragment(private val article: Article, private val senderFragmentTag: String) : Fragment() {

    /**
     * Creates and returns the view hierarchy associated with the fragment.
     *
     * @param inflater The layout inflater associated with the fragment.
     * @param container The fragment container.
     * @param savedInstanceState The saved state of the fragment.
     * @return The view hierarchy associated with the fragment.
     */
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.article_viewer_fragment, container, false)
    }

    /**
     * Initialises the web view and displays the article.
     * Sets up navigation.
     *
     * @param view The view hierarchy associated with the fragment.
     * @param savedInstanceState The saved state of the fragment.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val webView = view.findViewById<WebView>(R.id.articleViewerWebView)
        // Prevent web view from opening browser app
        webView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return false
            }
        })

        if (article.getUrl() != null) {
            webView.loadUrl(article.getUrl()!!)
        } else {
            navigateToSenderFragment()
        }

        val bottomNavigation = activity!!.findViewById<View>(R.id.bottom_navigation) as BottomNavigationView
        // Allow alterations to checked menu items
        bottomNavigation.menu.setGroupCheckable(0, true, false)
        // Uncheck all possible fragments navigated from
        bottomNavigation.menu.findItem(R.id.home).isChecked = false
        bottomNavigation.menu.findItem(R.id.search).isChecked = false
        bottomNavigation.menu.findItem(R.id.games).isChecked = false
        // Prevent alterations to checked menu items
        bottomNavigation.menu.setGroupCheckable(0, true, true)
    }

    /**
     * Sets up navigation if the back button is pressed.
     *
     * @param savedInstanceState The saved state of the fragment.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Navigate back to the sender fragment if the back button is pressed
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true ) {
            override fun handleOnBackPressed() {
                navigateToSenderFragment()
                val bottomNavigation = activity!!.findViewById<View>(R.id.bottom_navigation) as BottomNavigationView
                bottomNavigation.menu.setGroupCheckable(0, true, false)
                if (senderFragmentTag == Constants.HOME_FRAGMENT_TAG) {
                    bottomNavigation.menu.findItem(R.id.home).isChecked = true
                } else if (senderFragmentTag == Constants.GAMES_FRAGMENT_TAG) {
                    bottomNavigation.menu.findItem(R.id.games).isChecked = true
                } else if (senderFragmentTag == Constants.SEARCH_FRAGMENT_TAG) {
                    bottomNavigation.menu.findItem(R.id.search).isChecked = true
                }
                bottomNavigation.menu.setGroupCheckable(0, true, true)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    /**
     * Takes the user back to the sender fragment
     */
    private fun navigateToSenderFragment() {
        var fragment = Fragment()
        if (senderFragmentTag == Constants.HOME_FRAGMENT_TAG) {
            fragment = HomeFragment()
        } else if (senderFragmentTag == Constants.GAMES_FRAGMENT_TAG) {
            fragment = GamesFragment()
        } else if (senderFragmentTag == Constants.SEARCH_FRAGMENT_TAG) {
            fragment = SearchFragment()
        }
        val fragmentManager = activity!!.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_main, fragment, senderFragmentTag)
        fragmentTransaction.commit()
    }
}