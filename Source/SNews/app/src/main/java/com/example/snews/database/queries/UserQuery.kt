package com.example.snews.database.queries

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class UserQuery(val db: FirebaseFirestore) {

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