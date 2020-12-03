package com.example.snews.models

//TODO - Null safety
/**
 * Source model for holding source data on a particular article.
 *
 * @property id The id of the news company.
 * @property name The name of the news company.
 * @author Samuel Netherway
 */
class Source (id: String?, name: String) {

    private var id: String? = null
    private var name: String? = null

    /**
     * @constructor Initializes information about the source.
     */
    init {
        this.id = id
        this.name = name
    }

    /**
     * @return The source id of the article, e.g. cnn, usa-today, cbs-news, etc.
     */
    fun getID() : String? {
        return this.id
    }

    /**
     * @return The name of the source which the article is from, e.g. CNN, USA Today, CBS News, etc.
     */
    fun getName() : String? {
        return this.name
    }
}