package com.example.snews.fragments

import android.content.ContentValues
import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.snews.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

//TODO - If a new user logs in run article refresh service
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
        val spotlightWordEditText = view.findViewById<EditText>(R.id.spotlightWordEditText)
        val addSpotlightChipButton = view.findViewById<Button>(R.id.addSpotlightChip)
        val spotlightChipGroup = view.findViewById<ChipGroup>(R.id.spotlightChipGroup)

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

        addSpotlightChipButton.setOnClickListener {
            var word = spotlightWordEditText.text.toString()
            var chip: Chip = createNewChip(word, spotlightChipGroup)
            spotlightChipGroup.addView(chip)
        }
    }

    //TODO - Documentation
    /**
     *
     */
    private fun createNewChip(text: String, chipGroup: ChipGroup) : Chip {
        var chip = Chip(this.requireActivity())
        chip.text = text
        chip.chipIcon = ContextCompat.getDrawable(this.requireContext(),R.drawable.ic_no_article_image) // TODO - Replace
        chip.isCheckable = true
        chip.isClickable = true
        chip.isCloseIconVisible = true
        chip.setOnClickListener{
            Toast.makeText(this.requireContext(), "Clicked: " + chip.text, Toast.LENGTH_LONG).show()
        }
        chip.setOnCloseIconClickListener{
            // Smoothly remove chip from chip group
            TransitionManager.beginDelayedTransition(chipGroup)
            Toast.makeText(this.requireContext(), "Removed: " + chip.text, Toast.LENGTH_LONG).show()
            chipGroup.removeView(chip)
        }
        return chip
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
    private fun navigateToSignInRegisterFragment() {
        val signInRegisterFragment = SignInRegisterFragment(mAuth, db)
        val fragmentManager = activity!!.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_main, signInRegisterFragment, "SignInRegisterFragment") //TODO - Check what the value of the tag paramaeter is meant to be
        fragmentTransaction.commit()
    }

    //TODO - Implement or remove
    //TODO - Documentation
    /**
     *
     *
     * @param view
     */
    private fun updateUISignedIn(view: View) {
        val signInRegister = view.findViewById<Button>(R.id.signInRegisterButton)
        val loggedInStatus = view.findViewById<TextView>(R.id.loggedInStatus)
        signInRegister.setText("Sign Out") //TODO - Use external string
        // TODO - Set loggedInStatus to be the first_name and last_name of the user
    }
}
