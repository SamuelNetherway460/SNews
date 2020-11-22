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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

//TODO - Toast messages if there is a class between spotlight and hide filtering. Ask user to remove one first.
//TODO - Full XML Check
//TODO - Implement shared preferences for storage of device specific preferences
//TODO - Add signed in check
//TODO - Documentation
/**
 *
 *
 * @author Samuel Netherway
 */
class ProfileFragment(private val tAuth: FirebaseAuth, private val db: FirebaseFirestore) : Fragment() {

    //TODO - Documentation
    /**
     *
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

        if (tAuth.currentUser != null) {
            signInRegister.setText("Sign Out") //TODO - Externalise string
        } else {
            signInRegister.setText("Sign in / Register") //TODO - Externalise string
        }

        signInRegister.setOnClickListener {
            if (tAuth.currentUser != null) {
                tAuth.signOut()
                signInRegister.setText("Sign in / Register") //TODO - Externalise string
                loggedInStatus.setText("Sign in or Register") //TODO - Externalise string
            } else {
                navigateToSignInRegisterFragment()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val signInRegister = view?.findViewById<Button>(R.id.signInRegisterButton)
        val loggedInStatus = view?.findViewById<TextView>(R.id.loggedInStatus)
        if (tAuth.currentUser != null) {
            if (signInRegister != null) signInRegister.setText("Sign Out") //TODO - Externalise string
            if (signInRegister != null && loggedInStatus != null) loggedInStatus.setText(tAuth.currentUser!!.email) //TODO - Check null safety
        } else {
            if (signInRegister != null) signInRegister.setText("Sign In / Register") //TODO - Externalise string
        }
    }

    fun navigateToSignInRegisterFragment() {
        val signInRegisterFragment = SignInRegisterFragment(tAuth, db)
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
