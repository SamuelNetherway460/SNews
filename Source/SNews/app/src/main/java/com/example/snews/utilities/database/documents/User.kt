package com.example.snews.utilities.database.documents

class User (uid: String?){

    private var uid: String? = null
    private var categories: Array<String>? = null

    init {
        this.uid = uid
    }

    fun getUID() : String? {
        return this.uid
    }

    fun getCategories() : Array<String>? {
        return this.categories
    }

    fun setCategories(categories: Array<String>) {
        this.categories = categories
    }
}