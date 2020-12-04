package com.example.snews.fragments

import android.content.ContentValues
import android.content.Intent
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
import com.example.snews.models.Article
import com.example.snews.models.ArticleGroup
import com.example.snews.services.FetchArticleService
import com.example.snews.utilities.parsers.ArticleParser
import org.json.JSONObject

//TODO - Implement callback to refresh recycler view content when the article fetching service runs
//TODO - If no discover publishers or categories are selected. Do top headlines as default.
//TODO - Full XML Check
//TODO - Documentation
//TODO - Implement all relevant on's
/**
 * Fragment responsible for displaying article data.
 *
 * @author Samuel Netherway
 */
class HomeFragment : Fragment() {

    //TODO - Externalise
    private val ARTICLE_STORE_FILENAME = "articleData"

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
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    /**
     * Sets up the recycler view to display news data.
     *
     * @param view The view hierarchy associated with the fragment.
     * @param savedInstanceState The saved state of the fragment.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //startRecyclerView(view, getArticles()) //TODO - iffy possibly uncomment later
    }

    //TODO - Implement or remove
    /**
     *
     */
    override fun onPause() {
        super.onPause()
        Log.d(ContentValues.TAG, "HOME FRAGMENT - ON PAUSE CALLED")
    }

    //TODO - Implement or remove
    /**
     *
     */
    override fun onResume() {
        super.onResume()
        Log.d(ContentValues.TAG, "HOME FRAGMENT - ON RESUME CALLED")
        startRecyclerView(this.requireView(), getArticles()) //TODO - iffy remove later
    }

    //TODO - Implement or remove
    /**
     *
     */
    override fun onStop() {
        super.onStop()
        Log.d(ContentValues.TAG, "HOME FRAGMENT - ON STOP CALLED")
    }

    //TODO - Possibly remove after testing has concluded
    override fun onDestroy() {
        super.onDestroy()
    }

    //TODO - Implement or remove
    /**
     *
     */
    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(ContentValues.TAG, "HOME FRAGMENT - ON DESTROY CALLED")
    }

    /**
     * Read and parse the article data from internal storage.
     *
     * @return A list of articles.
     */
    fun getArticles(): ArrayList<Article> {
        var articles = ArrayList<Article>()
        if (fileExist(ARTICLE_STORE_FILENAME)) {
            var jsonArticles = JSONObject(readArticleStorage()).getJSONArray("data")
            for (i in 0..jsonArticles.length() - 1) {
                articles.add(ArticleParser.parseArticle(jsonArticles.getJSONObject(i)))
            }
        }
        return articles
    }

    /**
     * Checks whether a file exists in internal storage.
     *
     * @param filename The name of the file which is being check for.
     * @return A boolean indicating whether the file exists in internal storage or not.
     */
    private fun fileExist(filename: String): Boolean {
        val file = context!!.getFileStreamPath(filename)
        return file.exists()
    }

    /**
     * Reads the article data from internal storage.
     *
     * @return The raw article data.
     */
    private fun readArticleStorage() : String {
        activity!!.openFileInput(ARTICLE_STORE_FILENAME).use {
            return it.readBytes().decodeToString()
        }
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
    fun startRecyclerView(view: View, articles: ArrayList<Article>) {
        var recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        // Start recylcer view population
        recyclerView.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            adapter = RecyclerAdapter(articles, activity!!)
        }
    }
}