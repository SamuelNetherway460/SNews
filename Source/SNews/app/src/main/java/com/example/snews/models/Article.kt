package com.example.snews.models

import org.json.JSONObject

//TODO - Null Safety
//TODO - Correctly format publishedAt date time
/**
 * Article model for holding data on a single article.
 *
 * @property json Raw JSON data for the article.
 * @property source A source object containing information about the article source.
 * @property author The author of the article.
 * @property title The title of the article.
 * @property description A short description about the article.
 * @property url The url of the web page which contains the article content.
 * @property urlToImage The url of the image associated with the article.
 * @property publishedAt The date and time that the article was published.
 * @property content The article content truncated to 200 characters.
 * @author Samuel Netherway
 */
class Article (json: JSONObject, source: Source?, author: String?, title: String?, description: String?, url: String?,
               urlToImage: String?, publishedAt: String?, content: String?) {

    //TODO - Externalise strings
    private var json: JSONObject
    private var source: Source? = null //TODO - Null Safety
    private var author: String = "No Author"
    private var title: String = "No Title"
    private var description: String = "No Description"
    private var url: String = "No URL"
    private var urlToImage: String = "No URL to Image"
    private var publishedAt: String = "No Publish Date"
    private var content: String = "No Content"

    /**
     * @constructor Initializes information about the articles.
     */
    init {
        this.json = json
        this.source = source //TODO - Null Safety
        if (author != null) this.author = author
        if (title != null) this.title = title
        if (description != null) this.description = description
        if (url != null) this.url = url
        if (urlToImage != null) this.urlToImage = urlToImage
        if (publishedAt != null) this.publishedAt = publishedAt
        if (content != null) this.content = content
    }

    //TODO - Documentation
    /**
     * @return
     */
    fun getJSON() : JSONObject {
        return this.json
    }

    /**
     * @return A source object containing information about the article source.
     */
    fun getSource() : Source? {
        return this.source
    }

    /**
     * @return The author of the article.
     */
    fun getAuthor() : String? {
        return this.author
    }

    /**
     * @return The title of the article.
     */
    fun getTitle() : String? {
        return this.title
    }

    /**
     * @return A short description about the article.
     */
    fun getDescription() : String? {
        return this.description
    }

    /**
     * @return The url of the web page which contains the article content.
     */
    fun getUrl() : String? {
        return this.url
    }

    /**
     * @return The url of the image associated with the article.
     */
    fun getUrlToImage() : String? {
        return this.urlToImage
    }

    /**
     * @return The date and time that the article was published.
     */
    fun getPublishedAt() : String? {
        return this.publishedAt
    }

    /**
     * @return The article content truncated to 200 characters.
     */
    fun getContent() : String? {
        return this.content
    }
}