package com.example.snews.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.snews.MainActivity
import com.example.snews.R
import com.example.snews.adapters.RecyclerAdapter
import com.example.snews.models.Article
import com.example.snews.utilities.Constants
import com.example.snews.utilities.formatters.NewsAPIRequestFormatter
import com.example.snews.utilities.parsers.ArticleParser
import com.koushikdutta.ion.Ion
import org.json.JSONObject

/**
 * Fragment responsible for allowing the user to perform custom key word searches.
 *
 * @author Samuel netherway
 */
class SearchFragment : Fragment() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private var searchView: SearchView? = null

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
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchView = view.findViewById(R.id.searchView)

        searchView!!.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (searchView!!.query.toString() != null) {
                    if (searchView!!.query.toString() != Constants.EMPTY_STRING) {
                        fetchAndDisplayArticles(searchView!!.query.toString())
                    }
                }
                searchView!!.setQuery("", false);
                searchView!!.clearFocus();
                return true
            }
        })
    }

    /**
     * Fetches the articles from the News API and starts the recycler view.
     */
    private fun fetchAndDisplayArticles(searchedString: String) {
        if (searchedString.contains(Constants.SPACE)) {
            Toast.makeText(
                    this.requireContext(),
                    resources.getString(R.string.remove_spaces),
                    Toast.LENGTH_LONG
            ).show()
        } else {
            var request = NewsAPIRequestFormatter.generateRequestFromKeyWord(searchedString)
            Log.d(TAG, request.getURL())
            Ion.with(this)
                    .load(Constants.GET, request.getURL())
                    .setHeader("user-agent", "insomnia/2020.4.1")
                    .asString()
                    .setCallback { ex, result ->
                        var articleGroup = ArticleParser.parseArticleGroup(JSONObject(result))
                        if (articleGroup.getArticles() != null) {
                            startRecyclerView(view!!, articleGroup.getArticles()!!)
                        }
                    }
        }
    }

    /**
     * Sets up the recycler view with article data.
     *
     * @param view The current view hierarchy associated with the fragment.
     * @param articles An array list of article data.
     */
    private fun startRecyclerView(view: View, articles: ArrayList<Article>) {
        var recyclerView = view.findViewById<RecyclerView>(R.id.searchRecyclerView)
        // Start recycler view population
        recyclerView!!.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = RecyclerAdapter(articles, activity!!, Constants.SEARCH_FRAGMENT_TAG)
        }
    }
}