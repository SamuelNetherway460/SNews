package com.example.snews.utilities.database.documents

//TODO - Documentation
//TODO - Finish Implementation
//TODO - Null safety
/**
 *
 *
 * @author Samuel Netherway
 */
class User (uid: String?){

    private var uid: String? = null
    private var email: String? = null
    private var password: String? = null
    private var firstName: String? = null
    private var lastName: String? = null
    private var categories: Array<String>? = null
    private var publishers: Array<String>? = null

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