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

class ProfileFragment : Fragment() {

    private var mAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email = view.findViewById<EditText>(R.id.enterEmail)
        val password = view.findViewById<EditText>(R.id.enterPassword)
        val loginButton = view.findViewById<Button>(R.id.loginButton)
        val loggedInStatus = view.findViewById<TextView>(R.id.loggedInStatus)

        loggedInStatus.setText("Hello")

        loginButton.setOnClickListener { view ->
            mAuth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this.requireActivity(),
                    OnCompleteListener<AuthResult?> { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = mAuth.currentUser
                            var uid = user!!.uid
                            loggedInStatus.setText("LOGGED IN" + uid)
                        } else {
                            loggedInStatus.setText("FAILURE")
                            // If sign in fails, display a message to the user.
                        }
                    })
        }
    }

    fun updateUI(user: FirebaseUser?, view: View) {
        val loggedInStatus = view.findViewById<TextView>(R.id.loggedInStatus)
        if (user == null) {
            loggedInStatus.setText("LOGIN FAILURE")
        } else {
            loggedInStatus.setText("LOGGED IN")
        }
    }
}
