package com.example.snews.models

//TODO - Null Safety
/**
 * Article model for holding data on a single article.
 *
 * @author Samuel Netherway
 */
class Article (source: Source?, author: String?, title: String?, description: String?, url: String?,
               urlToImage: String?, publishedAt: String?, content: String?) {

    //TODO - Externalise strings
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
        this.source = source //TODO - Null Safety
        if (author != null) this.author = author
        if (title != null) this.title = title
        if (description != null) this.description = description
        if (url != null) this.url = url
        if (urlToImage != null) this.urlToImage = urlToImage
        if (publishedAt != null) this.publishedAt = publishedAt
        if (content != null) this.content = content
    }

    /**
     * @return The article source.
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
     * @return A description of the article.
     */
    fun getDescription() : String? {
        return this.description
    }

    /**
     * @return The url of the article.
     */
    fun getUrl() : String? {
        return this.url
    }

    /**
     * @return A url to the image associated with the article.
     */
    fun getUrlToImage() : String? {
        return this.urlToImage
    }

    /**
     * @return The publisher of the article.
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