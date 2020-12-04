package com.example.snews.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.snews.R
import com.example.snews.models.Article
import com.google.android.material.bottomnavigation.BottomNavigationView

//TODO - Organises on's in order of being called
//TODO - Consider all on's
//TODO - Look into savedInstanceState
//TODO - Documentation
//TODO - Implement
/**
 * Fragment responsible for displaying news articles.
 *
 * @property article The article to be displayed
 * @author Samuel Netherway
 */
class ArticleViewerFragment(private val article: Article) : Fragment() {

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
            navigateToHomeFragment()
        }

        val bottomNavigation = activity!!.findViewById<View>(R.id.bottom_navigation) as BottomNavigationView
        // Allow alterations to checked menu items
        bottomNavigation.menu.setGroupCheckable(0, true, false)
        // Uncheck home
        bottomNavigation.menu.findItem(R.id.home).isChecked = false
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
        // Navigate back to the home fragment if the back buttion is pressed
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true ) {
            override fun handleOnBackPressed() {
                navigateToHomeFragment()
                val bottomNavigation = activity!!.findViewById<View>(R.id.bottom_navigation) as BottomNavigationView
                bottomNavigation.menu.setGroupCheckable(0, true, false)
                bottomNavigation.menu.findItem(R.id.home).isChecked = true
                bottomNavigation.menu.setGroupCheckable(0, true, true)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    //TODO - Implement or remove
    /**
     *
     */
    override fun onPause() {
        super.onPause()
        Log.d(ContentValues.TAG, "ARTICLE VIEWER - ON PAUSE CALLED")
    }

    //TODO - Implement or remove
    /**
     *
     */
    override fun onResume() {
        super.onResume()
        Log.d(ContentValues.TAG, "ARTICLE VIEWER - ON RESUME CALLED")
    }

    //TODO - Implement or remove
    /**
     *
     */
    override fun onStop() {
        super.onStop()
        Log.d(ContentValues.TAG, "ARTICLE VIEWER - ON STOP CALLED")
    }

    //TODO - Implement or remove
    /**
     *
     */
    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(ContentValues.TAG, "ARTICLE VIEWER - ON DESTROY CALLED")
    }

    /**
     * Takes the user to the home fragment
     */
    private fun navigateToHomeFragment() {
        val homeFragment = HomeFragment()
        val fragmentManager = activity!!.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        //TODO - should fl_main be this fragment
        fragmentTransaction.replace(R.id.fl_main, homeFragment, "HomeFragment") //TODO - Check what the value of the tag parameter is meant to be
        fragmentTransaction.commit()
    }
}