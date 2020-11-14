package com.example.snews

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity  : AppCompatActivity() {

    private var mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val email = findViewById<EditText>(R.id.enterEmail)
        val password = findViewById<EditText>(R.id.enterPassword)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener { view ->
            mAuth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this,
                    OnCompleteListener<AuthResult?> { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = mAuth.currentUser
                            updateUI(user, view)
                        } else {
                            // If sign in fails, display a message to the user.
                            updateUI(null, view)
                        }
                    })
        }
    }

    fun updateUI(user: FirebaseUser?, view: View) {
        val loggedInStatus = view.findViewById<TextView>(R.id.loggedInStatus)
        if (user == null) {
            loggedInStatus.text = "LOGIN FAILED"
        } else {
            loggedInStatus.text = "LOGGED IN"
        }
    }
}