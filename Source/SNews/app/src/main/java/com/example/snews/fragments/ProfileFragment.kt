package com.example.snews.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.snews.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

//TODO - Toast messages if there is a class between spotlight and hide filtering. Ask user to remove one first.
//TODO - Full XML Check
//TODO - Implement shared preferences for storage of device specific preferences
//TODO - Add signed in check
//TODO - Documentation
/**
 * Fragment responsible for managing the user account and app preferences.
 *
 * @author Samuel Netherway
 */
class ProfileFragment(private val mAuth: FirebaseAuth, private val db: FirebaseFirestore) : Fragment() {

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
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    //TODO - Documentation
    /**
     *
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signInRegister = view.findViewById<Button>(R.id.signInRegisterButton)
        val loggedInStatus = view.findViewById<TextView>(R.id.loggedInStatus)

        if (mAuth.currentUser != null) {
            signInRegister.setText("Sign Out") //TODO - Externalise string
        } else {
            signInRegister.setText("Sign in / Register") //TODO - Externalise string
        }

        signInRegister.setOnClickListener {
            if (mAuth.currentUser != null) {
                mAuth.signOut()
                signInRegister.setText("Sign in / Register") //TODO - Externalise string
                loggedInStatus.setText("Sign in or Register") //TODO - Externalise string
            } else {
                navigateToSignInRegisterFragment()
            }
        }
    }

    //TODO - Documentation
    /**
     *
     */
    override fun onStart() {
        super.onStart()
        val signInRegister = view?.findViewById<Button>(R.id.signInRegisterButton)
        val loggedInStatus = view?.findViewById<TextView>(R.id.loggedInStatus)
        if (mAuth.currentUser != null) {
            if (signInRegister != null) signInRegister.setText("Sign Out") //TODO - Externalise string
            if (signInRegister != null && loggedInStatus != null) loggedInStatus.setText(mAuth.currentUser!!.email) //TODO - Check null safety and change to users first and last name
        } else {
            if (signInRegister != null) signInRegister.setText("Sign In / Register") //TODO - Externalise string
        }
    }

    //TODO - Implement or remove
    /**
     *
     */
    override fun onPause() {
        super.onPause()
        Log.d(ContentValues.TAG, "PROFILE FRAGMENT - ON PAUSE CALLED")
    }

    //TODO - Implement or remove
    /**
     *
     */
    override fun onResume() {
        super.onResume()
        Log.d(ContentValues.TAG, "PROFILE FRAGMENT - ON RESUME CALLED")
    }

    //TODO - Implement or remove
    /**
     *
     */
    override fun onStop() {
        super.onStop()
        Log.d(ContentValues.TAG, "PROFILE FRAGMENT - ON STOP CALLED")
    }

    //TODO - Implement or remove
    /**
     *
     */
    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(ContentValues.TAG, "PROFILE FRAGMENT - ON DESTROY CALLED")
    }

    //TODO - Documentation
    /**
     *
     */
    fun navigateToSignInRegisterFragment() {
        val signInRegisterFragment = SignInRegisterFragment(mAuth, db)
        val fragmentManager = activity!!.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_main, signInRegisterFragment, "SignInRegisterFragment") //TODO - Check what the value of the tag paramaeter is meant to be
        fragmentTransaction.commit()
    }

    //TODO - Implement
    //TODO - Documentation
    /**
     *
     *
     * @param view
     */
    fun updateUISignedIn(view: View) {
        val signInRegister = view.findViewById<Button>(R.id.signInRegisterButton)
        val loggedInStatus = view.findViewById<TextView>(R.id.loggedInStatus)
        signInRegister.setText("Sign Out") //TODO - Use external string
        // TODO - Set loggedInStatus to be the first_name and last_name of the user
    }
}
