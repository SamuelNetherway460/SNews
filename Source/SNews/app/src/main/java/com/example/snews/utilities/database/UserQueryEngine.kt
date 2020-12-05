package com.example.snews.utilities.database

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

//TODO - Possibly throw exception if the user cannot be found
/**
 * Class used to update various details in the user collection for a particular database.
 *
 * @author Samuel Netherway
 * @param db The FireStore database instance.
 */
class UserQueryEngine(val db: FirebaseFirestore) {

    companion object {
        private val DEFAULT_CATEGORIES = arrayListOf("business", "entertainment", "general")
        private val DEFAULT_PUBLISHERS = arrayListOf("wired", "tech crunch", "the next web")
    }

    /**
     * Adds a new user to the FireStore database.
     *
     * @param firstName The user's first name.
     * @param lastName The user's last name.
     * @param uid The user's unique identifier.
     */
    fun addUser(firstName: String, lastName: String, uid: String) {
        val user: MutableMap<String, Any> = HashMap()
        user["first_name"] = firstName
        user["last_name"] = lastName
        user["categories"] = DEFAULT_CATEGORIES
        user["publishers"] = DEFAULT_PUBLISHERS
        db.collection("users").document(uid).set(user)
    }

    /**
     * Adds a category to the user's list of selected categories.
     *
     * @param category The category to add to the user document.
     * @param uid The unique identifier for the user document.
     */
    fun addCategory(category: String, uid: String) {
        val user = db.collection("users").document(uid)
        user.update("categories", FieldValue.arrayUnion(category))
    }

    /**
     * Removes a category from the user's list of selected categories.
     *
     * @param category The category to remove from the user document.
     * @param uid The unique identifier for the user document.
     */
    fun removeCategory(category: String, uid: String) {
        val user = db.collection("users").document(uid)
        user.update("categories", FieldValue.arrayRemove(category))
    }

    /**
     * Adds a publisher to the user's list of selected publishers.
     *
     * @param publisher The publisher to add to the user document.
     * @param uid The unique identifier for the user document.
     */
    fun addPublisher(publisher: String, uid: String) {
        val user = db.collection("users").document(uid)
        user.update("publishers", FieldValue.arrayUnion(publisher))
    }

    /**
     * Removes a publisher from the user's list of selected publishers.
     *
     * @param publisher The publisher to remove from the user document.
     * @param uid The unique identifier for the user document.
     */
    fun removePublisher(publisher: String, uid: String) {
        val user = db.collection("users").document(uid)
        user.update("publishers", FieldValue.arrayRemove(publisher))
    }
}