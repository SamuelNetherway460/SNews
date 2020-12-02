package com.example.snews.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.snews.R
import androidx.recyclerview.widget.RecyclerView
import com.example.snews.adapters.RecyclerAdapter
import com.example.snews.utilities.parsers.ArticleParser
import com.koushikdutta.ion.Ion
import org.json.JSONObject

//TODO - If no discover publishers or categories are selected. Do top headlines as default.
//TODO - Full XML Check
//TODO - Documentation
/**
 *
 */
class HomeFragment : Fragment() {

    private val ARTICLE_STORE_FILENAME = getString(R.string.article_store_filename)

    //TODO - Documentation
    /**
     *
     */
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "ON CREATE VIEW CALLED")
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    //TODO - Documentation
    /**
     *
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchArticles(view)
        Log.d(TAG, "HOME - ON VIEW CREATED CALLED")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "HOME - ON PAUSE CALLED")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "HOME - ON RESUME CALLED")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "HOME - ON STOP CALLED")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "HOME - ON DESTROY CALLED")
    }

    //TODO - Documentation
    /**
     *
     */
    fun fetchArticles(view: View) {
        Ion.with(this)
                .load("GET", "https://newsapi.org/v2/top-headlines?sources=bbc-news&apiKey=d3629af64f934b1889b1fc3afb716b3c")
                .setHeader("user-agent", "insomnia/2020.4.1")
                .asString()
                .setCallback { ex, result ->
                    startRecyclerView(view, result)
                }
    }

    //TODO - Change method contents to match module recycler view demo
    //TODO - Documentation
    /**
     *
     */
    fun startRecyclerView(view: View, result: String) {
        var recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        // Start recylcer view population
        recyclerView.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            adapter = RecyclerAdapter(ArticleParser.parseArticleGroup(JSONObject(result)), activity!!)
        }
    }
}