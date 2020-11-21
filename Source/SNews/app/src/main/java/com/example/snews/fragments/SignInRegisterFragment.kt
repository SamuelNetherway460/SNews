package com.example.snews.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.snews.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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