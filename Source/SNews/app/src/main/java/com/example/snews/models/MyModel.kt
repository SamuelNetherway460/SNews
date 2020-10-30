package com.example.snews.models

class MyModel {

    var modelName: String? = null
    var modelDateTime: String? = null
    var modelCategory: String? = null
    private var modelImage: Int = 0

    /*
     * Return the article title
     */
    fun getArticleTitles(): String {
        return modelName.toString()
    }

    /*
     * Set a article title
     */
    fun setArticleTitles(name: String) {
        this.modelName = name
    }

    /*
    * Return the article date time
    */
    fun getDateTimes(): String {
        return modelDateTime.toString()
    }

    /*
     * Set a article date time
     */
    fun setDateTimes(name: String) {
        this.modelDateTime = name
    }

    /*
    * Return the article category
    */
    fun getCategories(): String {
        return modelCategory.toString()
    }

    /*
     * Set a article category
     */
    fun setCategories(name: String) {
        this.modelCategory = name
    }

    /*
     * Return a article image
     */
    fun getImages(): Int {
        return modelImage;
    }

    /*
     * Set a article image
     */
    fun setImages(image_drawable: Int) {
        this.modelImage = image_drawable
    }
}