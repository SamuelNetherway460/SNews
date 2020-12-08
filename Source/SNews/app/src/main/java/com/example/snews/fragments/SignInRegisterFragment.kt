package com.example.snews.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.snews.MainActivity
import com.example.snews.R
import com.example.snews.services.FetchArticleService
import com.example.snews.utilities.Constants
import com.example.snews.utilities.database.UserQueryEngine
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONArray
import org.json.JSONObject

/**
 * Fragment which allows users to sign in if they already have an account or register if they don't.
 *
 * @property mAuth The Firebase authentication instance.
 * @property db    Firestore instance.
 * @author Samuel netherway
 */
class SignInRegisterFragment(private val mAuth: FirebaseAuth, private val db: FirebaseFirestore)
    : Fragment() {

    /**
     * Creates and returns the view hierarchy associated with the fragment.
     *
     * @param inflater The layout inflater associated with the fragment.
     * @param container The fragment container.
     * @param savedInstanceState The saved state of the fragment.
     * @return The view hierarchy associated with the fragment.
     */
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sign_in_register_fragment, container, false)
    }

    /**
     * Initialising listeners for sign in and register buttons.
     *
     * @param view The view hierarchy associated with the fragment.
     * @param savedInstanceState The saved state of the fragment.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signInButton = view.findViewById<Button>(R.id.signInButton)
        val registerButton = view.findViewById<Button>(R.id.registerButton)

        signInButton.setOnClickListener { viewClicked ->
            signIn(view)
        }

        registerButton.setOnClickListener { viewClicked ->
            register(view)
        }
    }

    /**
     * Attempts to sign the user in with the provided credentials.
     *
     * @param view The view hierarchy associated with the fragment.
     */
    private fun signIn(view: View) {
        // Getting user details
        val emailSignIn = view.findViewById<EditText>(R.id.signInEmail)
        val passwordSignIn = view.findViewById<EditText>(R.id.signInPassword)
        val errorTextViewSignIn = view.findViewById<TextView>(R.id.signInErrorText)

        // Attempt to sign the user in with their email and password
        mAuth.signInWithEmailAndPassword(emailSignIn.text.toString(), passwordSignIn.text.toString())
                .addOnCompleteListener(this.requireActivity(),
                        OnCompleteListener<AuthResult?> { task ->
                            if (task.isSuccessful) {
                                overwriteInternalStoragePreferences(activity as MainActivity)
                            } else {
                                //TODO - Add user friendly messages
                                var errorMessage = task.exception?.message
                                if (errorMessage != null) {
                                    errorTextViewSignIn.setText(errorMessage)
                                } else {
                                    errorTextViewSignIn.setText(resources.getString(R.string.please_retry))
                                }
                            }
                        })
    }

    /**
     * Registers a new user.
     *
     * @param view The current view hierarchy associated with the fragment.
     */
    private fun register(view: View) {
        // Getting user details
        val registerErrorTextView = view.findViewById<TextView>(R.id.registerErrorText)
        val firstName = view.findViewById<EditText>(R.id.registerFirstName).text.toString()
        val lastName = view.findViewById<EditText>(R.id.registerLastName).text.toString()
        val registerEmail = view.findViewById<EditText>(R.id.registerEmail).text.toString()
        val registerEmailConfirm = view.findViewById<EditText>(R.id.registerConfirmEmail).text.toString()
        val registerPassword = view.findViewById<EditText>(R.id.registerPassword).text.toString()
        val registerPasswordConfirm = view.findViewById<EditText>(R.id.registerConfirmPassword).text.toString()

        if (firstName != null) {
            if (lastName != null) {
                if (registerEmail == registerEmailConfirm) {
                    if (registerPassword == registerPasswordConfirm) {
                        addNewUserToAuth(firstName, lastName, registerEmail, registerPassword)
                    } else {
                        registerErrorTextView.setText(resources.getString(R.string.password_does_not_match))
                    }
                } else {
                    registerErrorTextView.setText(resources.getString(R.string.email_does_not_match))
                }
            } else {
                registerErrorTextView.setText(resources.getString(R.string.invalid_last_name))
            }
        } else {
            registerErrorTextView.setText(resources.getString(R.string.invalid_first_name))
        }
    }

    /**
     * Adds the user's details to a new Firebase auth instance.
     *
     * @param firstName The user's first name.
     * @param lastName  The user's last name.
     * @param email     The user's email.
     * @param password  The user's selected password.
     */
    private fun addNewUserToAuth(firstName: String, lastName: String, email: String,
                                 password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this.requireActivity(),
                        OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        val user: FirebaseUser? = mAuth.currentUser
                        addNewUserToFireStore(firstName, lastName, user!!.uid)
                    }
                })
    }

    /**
     * Add's a new record to FireStore for the new user.
     *
     * @param firstName The user's first name.
     * @param lastName  The user's last name.
     * @param uid       The user's unique identifier generated by the Firebase auth.
     */
    private fun addNewUserToFireStore(firstName: String, lastName: String, uid: String) {
        val userQueryEngine = UserQueryEngine(db)
        userQueryEngine.addUser(firstName, lastName, uid)
        overwriteInternalStoragePreferences(activity as MainActivity)
    }

    /**
     * Overwrites the internal storage user preferences with Firestore preferences.
     *
     * @param activity The main activity.
     */
    private fun overwriteInternalStoragePreferences(activity: MainActivity) {
        fetchUserDocument(activity)
    }

    /**
     * Fetches the user's Firestore document and uses it to overwrite internal storage preferences.
     *
     * @param activity The main activity.
     */
    private fun fetchUserDocument(activity: MainActivity) {
        var uid: String? = mAuth.uid
        val user = db.collection(Constants.FIRESTORE_USERS_COLLECTION_PATH).document(uid!!)
        user.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        overwriteInternalCategories(document.get(Constants.FIRESTORE_CATEGORIES_FIELD)
                                as ArrayList<String>, activity)
                        overwriteInternalPublishers(document.get(Constants.FIRESTORE_PUBLISHERS_FIELD)
                                as ArrayList<String>, activity)
                        if (document.get(Constants.FIRESTORE_SPOTLIGHT_FIELD) != null) {
                            overwriteInternalSpotlightChips(document.get(
                                    Constants.FIRESTORE_SPOTLIGHT_FIELD) as Map<String, Boolean>, activity)
                        } else{
                            overwriteInternalSpotlightChips(null, activity)
                        }
                        if (document.get(Constants.FIRESTORE_HIDE_FIELD) != null) {
                            overwriteInternalHideChips(document.get(
                                    Constants.FIRESTORE_HIDE_FIELD) as Map<String, Boolean>, activity)
                        } else {
                            overwriteInternalHideChips(null, activity)
                        }
                        activity!!.startService(Intent(activity, FetchArticleService::class.java))
                        navigateToProfileFragment()
                    }
                }
    }

    /**
     * Overwrites the internal storage category preferences.
     *
     * @param firestoreSelectedCategories The Firestore copy of selected categories.
     * @param activity The main activity.
     */
    private fun overwriteInternalCategories(firestoreSelectedCategories: ArrayList<String>,
                                            activity: MainActivity) {
        var discoverPreferences = JSONObject(readInternalPreferences(activity))
        discoverPreferences.put(Constants.INTERNAL_CATEGORIES_JSON_ARRAY_NAME,
                JSONArray(firestoreSelectedCategories))
        writeInternalPreferences(discoverPreferences.toString(), activity)
    }

    /**
     * Overwrites the internal storage publisher preferences.
     *
     * @param firestoreSelectedPublishers The Firestore copy of selected publishers.
     * @param activity The main activity.
     */
    private fun overwriteInternalPublishers(firestoreSelectedPublishers: ArrayList<String>,
                                            activity: MainActivity) {
        var internalPreferences = JSONObject(readInternalPreferences(activity))
        internalPreferences.put(Constants.INTERNAL_PUBLISHERS_JSON_ARRAY_NAME,
                JSONArray(firestoreSelectedPublishers))
        writeInternalPreferences(internalPreferences.toString(), activity)
    }

    /**
     * Overwrites internal storage spotlight chip preferences with Firestore spotlight chip preferences.
     *
     * @param firestoreSpotlightChips A map containing all hide chip data.
     * @param activity The main activity.
     */
    private fun overwriteInternalSpotlightChips(firestoreSpotlightChips: Map<String, Boolean>?,
                                                activity: MainActivity) {
        var internalPreferences = JSONObject(readInternalPreferences(activity))
        var chips = JSONArray()

        if (firestoreSpotlightChips != null) {
            for (chip in firestoreSpotlightChips) {
                var chipJSON = JSONObject()
                chipJSON.put(Constants.INTERNAL_WORD_JSON_NAME, chip.key)
                chipJSON.put(Constants.INTERNAL_ENABLED_JSON_NAME, chip.value)
                chips.put(chipJSON)
            }
        }

        internalPreferences.put(Constants.INTERNAL_SPOTLIGHT_JSON_ARRAY_NAME, chips)
        writeInternalPreferences(internalPreferences.toString(), activity)
    }

    /**
     * Overwrites internal storage hide chip preferences with Firestore hide chip preferences.
     *
     * @param firestoreHideChips A map containing all hide chip data.
     * @param activity The main activity.
     */
    private fun overwriteInternalHideChips(firestoreHideChips: Map<String, Boolean>?,
                                           activity: MainActivity) {
        var internalPreferences = JSONObject(readInternalPreferences(activity))
        var chips = JSONArray()

        if (firestoreHideChips != null) {
            for (chip in firestoreHideChips) {
                var chipJSON = JSONObject()
                chipJSON.put(Constants.INTERNAL_WORD_JSON_NAME, chip.key)
                chipJSON.put(Constants.INTERNAL_ENABLED_JSON_NAME, chip.value)
                chips.put(chipJSON)
            }
        }

        internalPreferences.put(Constants.INTERNAL_HIDE_JSON_ARRAY_NAME, chips)
        writeInternalPreferences(internalPreferences.toString(), activity)
    }

    /**
     * Reads the user's discover preferences from internal storage.
     *
     * @param activity The main activity.
     * @return A string JSON containing the user's discover preferences.
     */
    private fun readInternalPreferences(activity: MainActivity) : String {
        activity!!.openFileInput(Constants.INTERNAL_PREFERENCES_FILENAME).use {
            return it.readBytes().decodeToString()
        }
    }

    /**
     * Write the user's discover preferences to internal storage.
     *
     * @param preferences A string JSON containing the user's discover preferences.
     */
    private fun writeInternalPreferences(preferences: String, activity: MainActivity) {
        activity!!.openFileOutput(Constants.INTERNAL_PREFERENCES_FILENAME, Context.MODE_PRIVATE).use {
            it.write(preferences.toByteArray())
        }
    }

    /**
     * Takes the user to the profile fragment.
     */
    private fun navigateToProfileFragment() {
        val profileFragment = ProfileFragment(mAuth, db)
        val fragmentManager = activity!!.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_main, profileFragment, Constants.PROFILE_FRAGMENT_TAG)
        fragmentTransaction.commit()
    }
}