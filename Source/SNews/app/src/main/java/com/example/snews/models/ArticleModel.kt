package com.example.snews.models

class ArticleModel {

    var articleTitle: String? = null
    var articleDateTime: String? = null
    var articleCategory: String? = null
    private var articleImage: Int = 0

    /*
     * Return the article title
     */
    fun getTitle(): String {
        return articleTitle.toString()
    }

    /*
     * Set a article title
     */
    fun setTitle(name: String) {
        this.articleTitle = name
    }

    /*
    * Return the article date time
    */
    fun getDateTime(): String {
        return articleDateTime.toString()
    }

    /*
     * Set a article date time
     */
    fun setDateTime(name: String) {
        this.articleDateTime = name
    }

    /*
    * Return the article category
    */
    fun getCategory(): String {
        return articleCategory.toString()
    }

    /*
     * Set a article category
     */
    fun setCategory(name: String) {
        this.articleCategory = name
    }

    /*
     * Return a article image
     */
    fun getImage(): Int {
        return articleImage;
    }

    /*
     * Set a article image
     */
    fun setImage(image_drawable: Int) {
        this.articleImage = image_drawable
    }
}