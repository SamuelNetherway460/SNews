package com.example.snews.fragments

import android.R.attr.password
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.snews.R
import com.example.snews.utilities.database.queryEngines.UserQueryEngine
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
 * @author Samuel netherway
 */
class SignInRegisterFragment(private val tAuth: FirebaseAuth, private val db: FirebaseFirestore) : Fragment() {

    //TODO - Documentation
    /**
     *
     */
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sign_in_register_fragment, container, false)
    }

    //TODO - Documentation
    /**
     *
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

    //TODO - Documentation
    /**
     * Attempts to sign the user in with the provided credentials.
     *
     * @param
     */
    fun signIn(view: View) {
        val emailSignIn = view.findViewById<EditText>(R.id.signInEmail)
        val passwordSignIn = view.findViewById<EditText>(R.id.signInPassword)
        val errorTextViewSignIn = view.findViewById<TextView>(R.id.signInErrorText)

        tAuth.signInWithEmailAndPassword(emailSignIn.text.toString(), passwordSignIn.text.toString())
                .addOnCompleteListener(this.requireActivity(),
                        OnCompleteListener<AuthResult?> { task ->
                            if (task.isSuccessful) {
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

    //TODO - Documentation
    //TODO - Implement function
    /**
     *
     *
     * @param view
     */
    fun register(view: View) {

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

    //TODO - Documentation
    /**
     *
     */
    fun addNewUserToAuth(firstName: String, lastName: String, email: String, password: String) {
        tAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this.requireActivity(),
                        OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        val user: FirebaseUser? = tAuth.getCurrentUser()
                        addNewUserToFireStore(firstName, lastName, user!!.uid) //TODO - Check null safety
                        navigateToProfileFragment()
                    } else {
                        //TODO - Implement
                    }
                })
    }

    //TODO - Documentation
    /**
     *
     */
    fun addNewUserToFireStore(firstName: String, lastName: String, uid: String) {
        val userQueryEngine = UserQueryEngine(db)
        userQueryEngine.addUser(firstName, lastName, uid)
    }

    /**
     * Takes the user to the profile fragment.
     */
    fun navigateToProfileFragment() {
        val profileFragment = ProfileFragment(tAuth, db)
        val fragmentManager = activity!!.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_main, profileFragment, "ProfileFragment") //TODO - Check what the value of the tag parameter is meant to be
        fragmentTransaction.commit()
    }
}