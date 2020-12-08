package com.example.snews.fragments

import android.content.ContentValues
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.snews.R
import com.example.snews.adapters.RecyclerAdapter
import com.example.snews.models.Article
import com.example.snews.utilities.Constants
import com.example.snews.utilities.parsers.ArticleParser
import org.json.JSONArray
import org.json.JSONObject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

//TODO - Full XML Check
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    /**
     * Sets up the recycler view to display news data.
     */
    override fun onResume() {
        super.onResume()
        Log.d(ContentValues.TAG, "HOME FRAGMENT - ON RESUME CALLED")
        var articles = getArticles()
        var filteredArticles = filterArticles(articles)
        startRecyclerView(this.requireView(), filteredArticles)
    }

    /**
     * Read and parse the article data from internal storage.
     *
     * @return A list of articles.
     */
    private fun getArticles(): ArrayList<Article> {
        var articles = ArrayList<Article>()
        if (fileExist(Constants.ARTICLE_STORE_FILENAME)) {
            var jsonArticles = JSONObject(readArticleStorage())
                    .getJSONArray(Constants.ARTICLE_STORE_JSON_ARRAY_NAME)
            for (i in 0..jsonArticles.length() - 1) {
                articles.add(ArticleParser.parseArticle(jsonArticles.getJSONObject(i)))
            }
        }
        return articles
    }

    // TODO - Finish, implement spotlight
    /**
     * Performs spotlight and hide filtering on an array of articles.
     *
     * @param articles The articles to be filtered.
     * @return A filtered array list of articles.
     */
    private fun filterArticles(articles: ArrayList<Article>) : ArrayList<Article> {
        var hideWords = getHideWords()
        var spotlightWords = getSpotlightWords()
        var newArticles = articles
        if (hideWords != null) {
            if (hideWords.size != 0) {
                newArticles = removeArticles(articles, hideWords)
            }
        }

        if (spotlightWords != null) {
            if (spotlightWords.size != 0) {
                newArticles = spotlightArticles(newArticles, spotlightWords)
            }
        }

        return newArticles
    }

    private fun spotlightArticles(articles: ArrayList<Article>, spotlightWords: ArrayList<String>)
            : ArrayList<Article> {
        var spotlightOrderedArticles = ArrayList<Article>()
        var articleRatings: HashMap<Article, Int> = HashMap()

        // Set all scores to zero
        for (article in articles) {
            articleRatings.set(article, 0)
        }

        // Calculate the score for each article based on the number of spotlight words are in the title
        for (article in articles) {
            for (word in spotlightWords) {
                if (article.getTitle() != null) {
                    if (article.getTitle()!!.toLowerCase().contains(word.toLowerCase())) {
                        var currentRating: Int = articleRatings[article]!!
                        currentRating++
                        articleRatings.set(article, currentRating)
                    }
                } else {
                    articleRatings.set(article, 0)
                }
            }
        }

        // Order articles
        val sortedArticles = articleRatings.toList().sortedBy { (_, value) -> value}.toMap()

        spotlightOrderedArticles = sortedArticles.keys.toMutableList() as ArrayList<Article>
        spotlightOrderedArticles.reverse()

        return spotlightOrderedArticles
    }

    /**
     * Removes articles which contain hide words.
     *
     * @param articles The articles being inspected.
     * @param hideWords The words being searched for.
     * @return A filtered array list of articles.
     */
    private fun removeArticles(articles: ArrayList<Article>, hideWords: ArrayList<String>) : ArrayList<Article> {
        var filteredArticles = ArrayList<Article>()
        var articlesToRemove = ArrayList<Int>()

        // Identify articles to remove
        for (article in articles) {
            for (word in hideWords) {
                if (article.getTitle() != null) {
                    if (article.getTitle()!!.toLowerCase().contains(word.toLowerCase())) {
                        if (!articlesToRemove.contains(articles.indexOf(article))) {
                            articlesToRemove.add(articles.indexOf(article))
                        }
                    }
                } else {
                    if (!articlesToRemove.contains(articles.indexOf(article))) {
                        articlesToRemove.add(articles.indexOf(article))
                    }
                }
            }
        }

        // Add articles to filtered articles which are not marked for removal
        for (article in articles) {
            if (!articlesToRemove.contains(articles.indexOf(article))) {
                filteredArticles.add(article)
            }
        }
        return filteredArticles
    }

    /**
     * Reads and returns the hide words from internal storage.
     *
     * @return An array list of hide words.
     */
    private fun getHideWords() : ArrayList<String> {
        var hideWords = ArrayList<String>()
        var internalPreferences = JSONObject(readInternalPreferences())
        var chipJSONS = jsonArrayToArrayList(
                internalPreferences.getJSONArray(Constants.INTERNAL_HIDE_JSON_ARRAY_NAME))

        for (chip in chipJSONS) {
            var word = chip.getString(Constants.INTERNAL_WORD_JSON_NAME)
            var enabled = chip.getBoolean(Constants.INTERNAL_ENABLED_JSON_NAME)
            if (enabled) hideWords.add(word)
        }

        return hideWords
    }

    /**
     * Reads and returns the spotlight words from internal storage.
     *
     * @return An array list of spotlight words.
     */
    private fun getSpotlightWords() : ArrayList<String> {
        var spotlightWords = ArrayList<String>()
        var internalPreferences = JSONObject(readInternalPreferences())
        var chipJSONS = jsonArrayToArrayList(
                internalPreferences.getJSONArray(Constants.INTERNAL_SPOTLIGHT_JSON_ARRAY_NAME))

        for (chip in chipJSONS) {
            var word = chip.getString(Constants.INTERNAL_WORD_JSON_NAME)
            var enabled = chip.getBoolean(Constants.INTERNAL_ENABLED_JSON_NAME)
            if (enabled) spotlightWords.add(word)
        }

        return spotlightWords
    }

    //TODO - Move to utilities class
    /**
     * Converts a JSON array of JSON object to a array list of JSON objects.
     *
     * @param jsonArray The JSON array to be converted.
     * @return An array list of JSON objects.
     */
    private fun jsonArrayToArrayList(jsonArray: JSONArray) : ArrayList<JSONObject> {
        var jsonArrayList = ArrayList<JSONObject>()
        for (i in 0..jsonArray.length() - 1) {
            jsonArrayList.add(jsonArray[i] as JSONObject)
        }
        return jsonArrayList
    }

    /**
     * Reads the user's preferences from the internal storage file.
     *
     * @return The user's preferences.
     */
    private fun readInternalPreferences() : String {
        context!!.openFileInput(Constants.INTERNAL_PREFERENCES_FILENAME).use {
            return it.readBytes().decodeToString()
        }
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
        // Start recycler view population
        recyclerView!!.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = RecyclerAdapter(articles, activity!!)
        }
    }
}