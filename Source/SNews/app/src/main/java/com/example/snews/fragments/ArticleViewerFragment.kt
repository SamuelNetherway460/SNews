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


//TODO - Documentation
//TODO - Implement
/**
 *
 *
 * @author Samuel Netherway
 */
class ArticleViewerFragment(private val article: Article) : Fragment() {

    //TODO - Documentation
    /**
     *
     */
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.article_viewer_fragment, container, false)
    }

    //TODO - Documentation
    /**
     *
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
        }
        val bottomNavigation = activity!!.findViewById<View>(R.id.bottom_navigation) as BottomNavigationView

        // Allow alterations to checked menu items
        bottomNavigation.menu.setGroupCheckable(0, true, false)
        // Uncheck home
        bottomNavigation.menu.findItem(R.id.home).isChecked = false
        // Prevent alterations to checked menu items
        bottomNavigation.menu.setGroupCheckable(0, true, true)
    }

    //TODO - Check
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                navigateToHomeFragment()
                val bottomNavigation = activity!!.findViewById<View>(R.id.bottom_navigation) as BottomNavigationView
                bottomNavigation.menu.setGroupCheckable(0, true, false)
                bottomNavigation.menu.findItem(R.id.home).isChecked = true
                bottomNavigation.menu.setGroupCheckable(0, true, true)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
        // The callback can be enabled or disabled here or in handleOnBackPressed()
    }

    override fun onPause() {
        super.onPause()
        Log.d(ContentValues.TAG, "ARTICLE VIEWER - ON PAUSE CALLED")
    }

    override fun onResume() {
        super.onResume()
        Log.d(ContentValues.TAG, "ARTICLE VIEWER - ON RESUME CALLED")
    }

    fun navigateToHomeFragment() {
        val homeFragment = HomeFragment()
        val fragmentManager = activity!!.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        //TODO - fl_main should be this fragment
        fragmentTransaction.replace(R.id.fl_main, homeFragment, "HomeFragment") //TODO - Check what the value of the tag parameter is meant to be
        fragmentTransaction.commit()
    }

    override fun onStop() {
        super.onStop()
        Log.d(ContentValues.TAG, "ARTICLE VIEWER - ON STOP CALLED")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(ContentValues.TAG, "ARTICLE VIEWER - ON DESTROY CALLED")
    }
}