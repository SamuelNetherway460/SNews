package com.example.snews.fragments

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.snews.R
import com.example.snews.services.FetchArticleService
import com.example.snews.utilities.database.UserQueryEngine
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

//TODO - Hide remove default error text and put in error handling and checking of user details, like check confirm email and password matches, etc
//TODO - Full XML Check
//TODO - Edit text entry color put in styles file
//TODO - Documentation
/**
 * Fragment which allows users to sign in if they already have an account or register if they don't.
 *
 * @property mAuth The Firebase authentication instance.
 * @property db    Firestore instance.
 * @author Samuel netherway
 */
class SignInRegisterFragment(private val mAuth: FirebaseAuth, private val db: FirebaseFirestore) : Fragment() {

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

    //TODO - Implement or remove
    /**
     *
     */
    override fun onPause() {
        super.onPause()
        Log.d(ContentValues.TAG, "SIGN IN / REGISTER FRAGMENT - ON PAUSE CALLED")
    }

    //TODO - Implement or remove
    /**
     *
     */
    override fun onResume() {
        super.onResume()
        Log.d(ContentValues.TAG, "SIGN IN / REGISTER FRAGMENT - ON RESUME CALLED")
    }

    //TODO - Implement or remove
    /**
     *
     */
    override fun onStop() {
        super.onStop()
        Log.d(ContentValues.TAG, "SIGN IN / REGISTER FRAGMENT - ON STOP CALLED")
    }

    //TODO - Implement or remove
    /**
     *
     */
    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(ContentValues.TAG, "SIGN IN / REGISTER FRAGMENT - ON DESTROY CALLED")
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
                                // Refresh articles based off the user's preferences
                                //TODO - Refresh internal storage preferences
                                context!!.startService(Intent(context, FetchArticleService::class.java))
                                navigateToProfileFragment()
                            } else {
                                //TODO - Add user friendly messages
                                var errorMessage = task.exception?.message
                                if (errorMessage != null) {
                                    errorTextViewSignIn.setText(errorMessage)
                                } else {
                                    errorTextViewSignIn.setText("Please retry") //TODO - Remove or externalise
                                }
                            }
                        })
    }

    //TODO - Test
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
                        registerErrorTextView.setText("Password does not match! Please enter a valid password.")
                    }
                } else {
                    registerErrorTextView.setText("Email does not match! Please enter a valid email address.")
                }
            } else {
                registerErrorTextView.setText("Please enter a valid last name")
            }
        } else {
            registerErrorTextView.setText("Please enter a valid first name.")
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
    private fun addNewUserToAuth(firstName: String, lastName: String, email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this.requireActivity(),
                        OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        val user: FirebaseUser? = mAuth.getCurrentUser()
                        addNewUserToFireStore(firstName, lastName, user!!.uid) //TODO - Check null safety
                        // Refresh articles based off the user's preferences
                        //TODO - Refresh internal storage preferences
                        context!!.startService(Intent(context, FetchArticleService::class.java))
                        navigateToProfileFragment()
                    } else {
                        //TODO - Implement
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
    }

    /**
     * Takes the user to the profile fragment.
     */
    private fun navigateToProfileFragment() {
        val profileFragment = ProfileFragment(mAuth, db)
        val fragmentManager = activity!!.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_main, profileFragment, "ProfileFragment") //TODO - Check what the value of the tag parameter is meant to be
        fragmentTransaction.commit()
    }
}