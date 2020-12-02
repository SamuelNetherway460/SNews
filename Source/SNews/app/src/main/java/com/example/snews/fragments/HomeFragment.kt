package com.example.snews.fragments

import android.app.Activity
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
import com.example.snews.models.ArticleGroup
import com.example.snews.utilities.parsers.ArticleParser
import org.json.JSONObject

//TODO - If no discover publishers or categories are selected. Do top headlines as default.
//TODO - Full XML Check
//TODO - Documentation
/**
 * Fragment responsible for displaying article data.
 *
 * @author Samuel Netherway
 */
class HomeFragment : Fragment() {

    //TODO - Externalise
    private val ARTICLE_STORE_FILENAME = "articleData"

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
        startRecyclerView(view, getArticles())
        Log.d(TAG, "HOME FRAGMENT - ON VIEW CREATED")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "HOME FRAGMENT - ON PAUSE CALLED")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "HOME FRAGMENT - ON RESUME CALLED")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "HOME FRAGMENT - ON STOP CALLED")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "HOME FRAGMENT - ON DESTROY CALLED")
    }

    /**
     * Read and parse the article data from internal storage.
     *
     * @return An article group object containing the parsed article data.
     */
    fun getArticles(): ArticleGroup {
        var rawNewsArticleData = readFromFile()
        return ArticleParser.parseArticleGroup(JSONObject((rawNewsArticleData)))
    }

    /**
     * Reads the article data from internal storage.
     *
     * @return The raw article data.
     */
    fun readFromFile() : String {
        //TODO - Null safety
        activity!!.openFileInput(ARTICLE_STORE_FILENAME).bufferedReader().useLines { lines ->
            lines.fold("") { some, text ->
                return text
            }
        }
        return "ERROR"
    }

    //TODO - Change method contents to match module recycler view demo
    //TODO - Documentation
    //TODO - Sort out comments
    /**
     * Sets up the recycler view with article data.
     *
     * @param view
     * @param articleGroup An article group object containing the article data.
     */
    fun startRecyclerView(view: View, articleGroup: ArticleGroup) {
        var recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        // Start recylcer view population
        recyclerView.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            adapter = RecyclerAdapter(articleGroup, activity!!)
        }
    }
}