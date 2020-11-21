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

        // TODO - Sign in check and update UI

        val signInRegister = view.findViewById<Button>(R.id.signInRegisterButton)

        signInRegister.setOnClickListener {
            navigateToSignInRegisterFragment()
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

    }
}
