package com.example.snews.utilities.database.queryEngines

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class UserQueryEngine(val db: FirebaseFirestore) {

    /**
     * Adds a category to the user's list of selected categories.
     */
    fun addCategory(uid: String, category: String) {
        val user = db.collection("users").document(uid)
        user.update("categories", FieldValue.arrayUnion(category))
    }

    /**
     * Removes a category from the user's list of selected categories.
     */
    fun removeCategory(uid: String, category: String) {
        val user = db.collection("users").document(uid)
        user.update("categories", FieldValue.arrayRemove(category))
    }
}