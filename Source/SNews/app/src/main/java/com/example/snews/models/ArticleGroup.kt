package com.example.snews.models

import com.example.snews.utilities.Constants

/**
 * Article group model for holding data on a multiple articles.
 *
 * @property status The status of the returned article data.
 * @property totalResults The total number of article associated with the API request.
 * @property articles The articles returned from the API request.
 * @author Samuel Netherway
 */
class ArticleGroup (status: String?, totalResults: Int?, articles: ArrayList<Article>?) {

    private var status: String = Constants.NO_STATUS
    private var totalResults: Int = Constants.NULL_RESULTS
    private var articles: ArrayList<Article>

    /**
     * @constructor Initializes information about an article group.
     */
    init {
        this.articles = ArrayList()
        if (status != null )this.status = status
        if (totalResults != null) this.totalResults = totalResults
        if (articles != null) this.articles = articles
    }

    /**
     * @return The status of the request for a group of articles from the News API.
     */
    fun getStatus () : String? {
        return this.status
    }

    /**
     * @return The total number of articles for a particular API request.
     */
    fun getTotalResults () : Int? {
        return this.totalResults
    }

    /**
     * @return A list of Article objects for the article group.
     */
    fun getArticles () : ArrayList<Article>? {
        return this.articles
    }
}