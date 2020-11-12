package com.example.snews.models

class ArticleGroup (status: String, totalResults: Int, articles: ArrayList<Article>) {

    private var status: String? = null
    private var totalResults: Int? = null
    private var articles: ArrayList<Article>? = null

    init {
        this.status = status
        this.totalResults = totalResults
        this.articles = articles
    }

    fun getStatus () : String? {
        return this.status
    }

    fun getTotalResults () : Int? {
        return this.totalResults
    }

    fun getArticles () : ArrayList<Article>? {
        return this.articles
    }
}