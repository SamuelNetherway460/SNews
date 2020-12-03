package com.example.snews.models

//TODO - Null Safety
/**
 * Article group model for holding data on a multiple articles.
 *
 * @property status The status of the returned article data.
 * @property totalResults The total number of article associated with the API request.
 * @property articles The articles returned from the API request.
 * @author Samuel Netherway
 */
class ArticleGroup (status: String?, totalResults: Int?, articles: ArrayList<Article>?) {

    private var status: String? = null
    private var totalResults: Int? = null
    private var articles: ArrayList<Article>? = null

    /**
     * @constructor Initializes information about an article group.
     */
    init {
        this.status = status
        this.totalResults = totalResults
        this.articles = articles
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