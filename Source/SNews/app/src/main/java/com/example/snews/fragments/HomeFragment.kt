package com.example.snews.fragments

import android.content.*
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
import com.example.snews.utilities.Constants
import com.example.snews.utilities.parsers.ArticleParser
import org.json.JSONObject

//TODO - If no discover publishers or categories are selected. Do top headlines as default.
//TODO - Full XML Check
//TODO - Implement all relevant on's
/**
 * Fragment responsible for displaying article data.
 *
 * @author Samuel Netherway
 */
class HomeFragment : Fragment() {

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
     */
    override fun onResume() {
        super.onResume()
        Log.d(ContentValues.TAG, "HOME FRAGMENT - ON RESUME CALLED")
        startRecyclerView(this.requireView(), getArticles())
    }

    /**
     * Read and parse the article data from internal storage.
     *
     * @return A list of articles.
     */
    private fun getArticles(): ArrayList<Article> {
        var articles = ArrayList<Article>()
        if (fileExist(Constants.ARTICLE_STORE_FILENAME)) {
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
        activity!!.openFileInput(Constants.ARTICLE_STORE_FILENAME).use {
            return it.readBytes().decodeToString()
        }
    }

    /**
     * Sets up the recycler view with article data.
     *
     * @param view The current view hierarchy associated with the fragment.
     * @param articles An array list of article data.
     */
    private fun startRecyclerView(view: View, articles: ArrayList<Article>) {
        var recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        // Start recylcer view population
        recyclerView!!.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = RecyclerAdapter(articles, activity!!)
        }
    }
}