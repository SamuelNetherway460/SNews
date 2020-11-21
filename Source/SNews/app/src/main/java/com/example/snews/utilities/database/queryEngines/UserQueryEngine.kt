package com.example.snews.utilities.database.queryEngines

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

//TODO - Possibly throw exception if the user cannot be found
/**
 * Class used to update various details in the user collection for a particular database.
 *
 * @author Samuel Netherway
 * @param db The FireStore database instance.
 * @param uid The user's unique identifier.
 */
class UserQueryEngine(val db: FirebaseFirestore, val uid: String) {

    private val user = db.collection("users").document(uid)

    /**
     * Adds a category to the user's list of selected categories.
     *
     * @param category The category to add to the user document.
     */
    fun addCategory(category: String) {
        user.update("categories", FieldValue.arrayUnion(category))
    }

    /**
     * Removes a category from the user's list of selected categories.
     *
     * @param category The category to remove from the user document.
     */
    fun removeCategory(category: String) {
        user.update("categories", FieldValue.arrayRemove(category))
    }

    /**
     * Adds a publisher to the user's list of selected publishers.
     *
     * @param publisher The publisher to add to the user document.
     */
    fun addPublisher(publisher: String) {
        user.update("publishers", FieldValue.arrayUnion(publisher))
    }

    /**
     * Removes a publisher from the user's list of selected publishers.
     *
     * @param publisher The publisher to remove from the user document.
     */
    fun removePublisher(publisher: String) {
        user.update("publishers", FieldValue.arrayRemove(publisher))
    }
}