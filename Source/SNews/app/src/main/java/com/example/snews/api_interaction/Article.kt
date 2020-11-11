package com.example.snews.api_interaction

class Article (title: String?) {

    private var title: String? = null

    init {
        this.title = title
    }

    /**
     * Returns the title of the article.
     */
    fun getTitle () : String? {
        return title
    }

    /**
     * Update the title of the article.
     */
    fun setTitle (title: String?) {
        this.title = title
    }
}