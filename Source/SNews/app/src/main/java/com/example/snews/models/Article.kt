package com.example.snews.models

class Article (source: Source?, author: String?, title: String?, description: String?, url: String?,
               urlToImage: String?, publishedAt: String?, content: String?) {

    private var source: Source? = null
    private var author: String? = null
    private var title: String? = null
    private var description: String? = null
    private var url: String? = null
    private var urlToImage: String? = null
    private var publishedAt: String? = null
    private var content: String? = null

    init {
        this.source = source
        this.author = author
        this.title = title
        this.description = description
        this.url = url
        this.urlToImage = urlToImage
        this.publishedAt = publishedAt
        this.content = content
    }

    fun getSource() : Source? {
        return this.source
    }

    fun getAuthor() : String? {
        return this.author
    }

    fun getTitle() : String? {
        return this.title
    }

    fun getDescription() : String? {
        return this.description
    }

    fun getUrl() : String? {
        return this.url
    }

    fun getUrlToImage() : String? {
        return this.urlToImage
    }

    fun getPublishedAt() : String? {
        return this.publishedAt
    }

    fun getContent() : String? {
        return this.content
    }
}