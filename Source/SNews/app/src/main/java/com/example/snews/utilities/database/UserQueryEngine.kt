package com.example.snews.utilities.database

import com.example.snews.utilities.Constants
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Class used to update various details in the user collection for a particular Firestore database.
 *
 * @author Samuel Netherway
 * @param db The FireStore database instance.
 */
class UserQueryEngine(val db: FirebaseFirestore) {

    companion object {
        private const val ACTIVE = true
        private const val INACTIVE = false
        private const val DOT = "."

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
        user[Constants.FIRESTORE_FIRST_NAME_FIELD] = firstName
        user[Constants.FIRESTORE_LAST_NAME_FIELD] = lastName
        user[Constants.FIRESTORE_CATEGORIES_FIELD] = DEFAULT_CATEGORIES
        user[Constants.FIRESTORE_PUBLISHERS_FIELD] = DEFAULT_PUBLISHERS
        db.collection(Constants.FIRESTORE_USERS_COLLECTION_PATH).document(uid).set(user)
    }

    /**
     * Adds a category to the user's list of selected categories.
     *
     * @param category The category to add to the user document.
     * @param uid The unique identifier for the user document.
     */
    fun addCategory(category: String, uid: String) {
        val user = db.collection(Constants.FIRESTORE_USERS_COLLECTION_PATH).document(uid)
        user.update(Constants.FIRESTORE_CATEGORIES_FIELD, FieldValue.arrayUnion(category))
    }

    /**
     * Removes a category from the user's list of selected categories.
     *
     * @param category The category to remove from the user document.
     * @param uid The unique identifier for the user document.
     */
    fun removeCategory(category: String, uid: String) {
        val user = db.collection(Constants.FIRESTORE_USERS_COLLECTION_PATH).document(uid)
        user.update(Constants.FIRESTORE_CATEGORIES_FIELD, FieldValue.arrayRemove(category))
    }

    /**
     * Adds a publisher to the user's list of selected publishers.
     *
     * @param publisher The publisher to add to the user document.
     * @param uid The unique identifier for the user document.
     */
    fun addPublisher(publisher: String, uid: String) {
        val user = db.collection(Constants.FIRESTORE_USERS_COLLECTION_PATH).document(uid)
        user.update(Constants.FIRESTORE_PUBLISHERS_FIELD, FieldValue.arrayUnion(publisher))
    }

    /**
     * Removes a publisher from the user's list of selected publishers.
     *
     * @param publisher The publisher to remove from the user document.
     * @param uid The unique identifier for the user document.
     */
    fun removePublisher(publisher: String, uid: String) {
        val user = db.collection(Constants.FIRESTORE_USERS_COLLECTION_PATH).document(uid)
        user.update(Constants.FIRESTORE_PUBLISHERS_FIELD, FieldValue.arrayRemove(publisher))
    }

    /**
     * Adds a new spotlight word to user's Firestore document.
     *
     * @param word The word to be added.
     * @param uid The unique identifier for the user document.
     */
    fun addSpotlightWord(word: String, uid: String) {
        val user = db.collection(Constants.FIRESTORE_USERS_COLLECTION_PATH).document(uid)
        user.update(mapOf(
                Constants.FIRESTORE_SPOTLIGHT_FIELD + DOT + word to ACTIVE
        ))
    }

    /**
     * Removes a spotlight word from the user's Firestore document.
     *
     * @param word The word to be removed.
     * @param uid The unique identifier for the user document.
     */
    fun removeSpotlightWord(word: String, uid: String) {
        val user = db.collection(Constants.FIRESTORE_USERS_COLLECTION_PATH).document(uid)
        user.update(mapOf(
                Constants.FIRESTORE_SPOTLIGHT_FIELD + DOT + word to FieldValue.delete()
        ))
    }

    /**
     * Removes all spotlight words from the user's Firestore document.
     *
     * @param uid The unique identifier for the user document.
     */
    fun removeAllSpotlightWords(uid: String) {
        val user = db.collection(Constants.FIRESTORE_USERS_COLLECTION_PATH).document(uid)
        user.update(mapOf(
                Constants.FIRESTORE_SPOTLIGHT_FIELD to FieldValue.delete()
        ))
    }

    /**
     * Enables a spotlight word in the user's Firestore document.
     *
     * @param word The word to be enabled.
     * @param uid The unique identifier for the user document.
     */
    fun enableSpotlightWord(word: String, uid: String) {
        val user = db.collection(Constants.FIRESTORE_USERS_COLLECTION_PATH).document(uid)
        user.update(mapOf(
                Constants.FIRESTORE_SPOTLIGHT_FIELD + DOT + word to ACTIVE
        ))
    }

    /**
     * Disables a spotlight word in the user's Firestore document.
     *
     * @param word The word to be disabled.
     * @param uid The unique identifier for the user document.
     */
    fun disableSpotlightWord(word: String, uid: String) {
        val user = db.collection(Constants.FIRESTORE_USERS_COLLECTION_PATH).document(uid)
        user.update(mapOf(
                Constants.FIRESTORE_SPOTLIGHT_FIELD + DOT + word to INACTIVE
        ))
    }

    /**
     * Adds a new hide word to user's Firestore document.
     *
     * @param word The word to be added.
     * @param uid The unique identifier for the user document.
     */
    fun addHideWord(word: String, uid: String) {
        val user = db.collection(Constants.FIRESTORE_USERS_COLLECTION_PATH).document(uid)
        user.update(mapOf(
                Constants.FIRESTORE_HIDE_FIELD + DOT + word to ACTIVE
        ))
    }

    /**
     * Removes a hide word from the user's Firestore document.
     *
     * @param word The word to be removed.
     * @param uid The unique identifier for the user document.
     */
    fun removeHideWord(word: String, uid: String) {
        val user = db.collection(Constants.FIRESTORE_USERS_COLLECTION_PATH).document(uid)
        user.update(mapOf(
                Constants.FIRESTORE_HIDE_FIELD + DOT + word to FieldValue.delete()
        ))
    }

    /**
     * Removes all hide words from the user's Firestore document.
     *
     * @param uid The unique identifier for the user document.
     */
    fun removeAllHideWords(uid: String) {
        val user = db.collection(Constants.FIRESTORE_USERS_COLLECTION_PATH).document(uid)
        user.update(mapOf(
                Constants.FIRESTORE_HIDE_FIELD to FieldValue.delete()
        ))
    }

    /**
     * Enables a hide word in the user's Firestore document.
     *
     * @param word The word to be enabled.
     * @param uid The unique identifier for the user document.
     */
    fun enableHideWord(word: String, uid: String) {
        val user = db.collection(Constants.FIRESTORE_USERS_COLLECTION_PATH).document(uid)
        user.update(mapOf(
                Constants.FIRESTORE_HIDE_FIELD + DOT + word to ACTIVE
        ))
    }

    /**
     * Disables a hide word in the user's Firestore document.
     *
     * @param word The word to be disabled.
     * @param uid The unique identifier for the user document.
     */
    fun disableHideWord(word: String, uid: String) {
        val user = db.collection(Constants.FIRESTORE_USERS_COLLECTION_PATH).document(uid)
        user.update(mapOf(
                Constants.FIRESTORE_HIDE_FIELD + DOT + word to INACTIVE
        ))
    }
}