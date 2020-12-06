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
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.example.snews.R
import com.example.snews.utilities.Constants
import com.example.snews.utilities.database.UserQueryEngine
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

//TODO - In the relevant on method, check that the current chip groups are correct based off the users account
//TODO - If a new user logs in run article refresh service
//TODO - Toast messages if there is a clash between spotlight and hide filtering. Ask user to remove one first.
//TODO - Full XML Check
//TODO - Implement shared preferences for storage of device specific preferences
//TODO - Add signed in check
/**
 * Fragment responsible for managing the user account and app preferences.
 *
 * @author Samuel Netherway
 */
class ProfileFragment(private val mAuth: FirebaseAuth, private val db: FirebaseFirestore) : Fragment() {

    companion object {
        const val SPOTLIGHT_CHIP = 0
        const val HIDE_CHIP = 1
    }

    var spotlightChipGroup: ChipGroup? = null
    var hideChipGroup: ChipGroup? = null

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

    /**
     * Performs setup for the fragment.
     *
     * @param view The view hierarchy associated with the fragment.
     * @param savedInstanceState The saved state of the fragment.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signInRegister = view.findViewById<Button>(R.id.signInRegisterButton)
        val loggedInStatus = view.findViewById<TextView>(R.id.loggedInStatus)

        val spotlightWordEditText = view.findViewById<EditText>(R.id.spotlightWordEditText)
        val addSpotlightChipButton = view.findViewById<Button>(R.id.addSpotlightChip)
        val spotlightRemoveAll = view.findViewById<Button>(R.id.spotlightRemoveAll)
        spotlightChipGroup = view.findViewById<ChipGroup>(R.id.spotlightChipGroup)

        val addHideChipButton = view.findViewById<Button>(R.id.addHideChip)
        val hideWordEditText = view.findViewById<EditText>(R.id.hideWordEditText)
        val hideRemoveAll = view.findViewById<Button>(R.id.hideRemoveAll)
        hideChipGroup = view.findViewById<ChipGroup>(R.id.hideChipGroup)

        if (mAuth.currentUser != null) {
            signInRegister.setText(resources.getString(R.string.sign_out))
        } else {
            signInRegister.setText(resources.getString(R.string.sign_in_register))
        }

        signInRegister.setOnClickListener {
            // Sign out current user
            if (mAuth.currentUser != null) {
                mAuth.signOut()
                signInRegister.setText(resources.getString(R.string.sign_in_register))
                loggedInStatus.setText(resources.getString(R.string.sign_in_or_register_instruction))
            } else {
                // Sign in or register user
                navigateToSignInRegisterFragment()
            }
        }

        // Adds a new spotlight chip
        addSpotlightChipButton.setOnClickListener {
            var word = spotlightWordEditText.text.toString().toLowerCase()
            addSpotlightChip(word)
            spotlightWordEditText.getText().clear()
        }

        // Adds a new hide chip
        addHideChipButton.setOnClickListener {
            var word = hideWordEditText.text.toString().toLowerCase()
            addHideChip(word)
            hideWordEditText.getText().clear()
        }

        // Delete all spotlight chips
        spotlightRemoveAll.setOnClickListener {
            spotlightChipGroup!!.removeAllViews()
            removeAllChipsFromStorage(SPOTLIGHT_CHIP)
        }

        // Delete all hide chips
        hideRemoveAll.setOnClickListener {
            hideChipGroup!!.removeAllViews()
            removeAllChipsFromStorage(HIDE_CHIP)
        }
    }

    /**
     * Updating various aspects of the fragment depending on if the user is signed in or not.
     */
    override fun onStart() {
        super.onStart()
        val signInRegister = view?.findViewById<Button>(R.id.signInRegisterButton)
        val loggedInStatus = view?.findViewById<TextView>(R.id.loggedInStatus)
        if (mAuth.currentUser != null) {
            if (signInRegister != null) signInRegister.setText(resources.getString(R.string.sign_out))
            if (signInRegister != null && loggedInStatus != null) loggedInStatus.setText(mAuth.currentUser!!.email)
            updateLoggedInUI()
            overwriteInternalStorage()
        } else {
            if (signInRegister != null) signInRegister.setText(resources.getString(R.string.sign_in_register))
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

    /**
     * Overwrites the internal storage copy of user preferences with the FireStore copy of the
     * user preference's.
     */
    private fun overwriteInternalStorage() {
        overwriteInternalCategories()
        overwriteInternalPublishers()
        overwriteInternalSpotlightChips()
        overwriteInternalHideChips()
    }

    //TODO - Implement
    //TODO - Documentation
    /**
     *
     */
    private fun overwriteInternalCategories() {

    }

    //TODO - Implement
    //TODO - Documentation
    /**
     *
     */
    private fun overwriteInternalPublishers() {

    }

    //TODO - Implement
    //TODO - Documentation
    /**
     *
     */
    private fun overwriteInternalSpotlightChips() {

    }

    //TODO - Implement
    //TODO - Documentation
    /**
     *
     */
    private fun overwriteInternalHideChips() {

    }

    //TODO - Documentation
    /**
     *
     */
    private fun updateLoggedInUI() {
        updateSpotlightChipsUI()
        updateHideChipsUI()
    }

    //TODO - Implement
    //TODO - Documentation
    /**
     *
     */
    private fun updateSpotlightChipsUI() {

    }

    //TODO - Implement
    //TODO - Documentation
    /**
     *
     */
    private fun updateHideChipsUI() {

    }

    /**
     * Takes the user to the screen which allows them to sign into their account or register for a
     * new account.
     */
    private fun navigateToSignInRegisterFragment() {
        val signInRegisterFragment = SignInRegisterFragment(mAuth, db)
        val fragmentManager = activity!!.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(
            R.id.fl_main,
            signInRegisterFragment,
            "SignInRegisterFragment"
        ) //TODO - Check what the value of the tag parameter is meant to be
        fragmentTransaction.commit()
    }

    /**
     * Creates and sets up a new chip.
     *
     * @param text The text which will be displayed on the chip.
     * @param chipGroup The chip group which the chip will be added to.
     * @return The new chip.
     */
    private fun createNewChip(text: String, chipType: Int, chipGroup: ChipGroup): Chip {
        var chip = Chip(this.requireActivity())

        if (chipType == SPOTLIGHT_CHIP) {
            chip.chipIcon =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_chip_spotlight)
        } else {
            chip.chipIcon =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_chip_hide)
        }

        chip.text = text
        chip.isCheckable = true
        chip.isChecked = true
        chip.isClickable = true
        chip.isCloseIconVisible = true
        chip.setCloseIconResource(R.drawable.ic_chip_remove)

        chip.setOnCheckedChangeListener { _, _ ->
            storeChipCheckedChange(chip, chipType)
        }

        chip.setOnCloseIconClickListener {
            removeChipFromStorage(chip, chipType)
            TransitionManager.beginDelayedTransition(chipGroup)
            chipGroup.removeView(chip)
        }
        return chip
    }

    /**
     * Checks if a chip with [word] as the text is in the [group] chip group.
     *
     * @param word The word which is being checked for.
     * @param group The chip group which is being searched through.
     * @return A boolean indicating whether an appropriate chip was found or not.
     */
    private fun checkIfChipExistsInGroup(word: String, group: ChipGroup) : Boolean {
        var chips = group.children as Sequence<Chip>
        for (chip in chips) {
            if (chip.text == word) return true
        }
        return false
    }

    /**
     * Creates and adds a new chip to the spotlight chip group.
     * Adds chip data to database and internal storage.
     *
     * @param word The word to be included in the chip.
     */
    private fun addSpotlightChip(word: String) {
        if (word != Constants.EMPTY_STRING) {
            // Check that the chip is not already in the spotlight group
            if (!checkIfChipExistsInGroup(word, spotlightChipGroup!!)) {
                // Check that the chip is not already in the hide group
                if (!checkIfChipExistsInGroup(word, hideChipGroup!!)) {
                    var chip: Chip = createNewChip(word, SPOTLIGHT_CHIP, spotlightChipGroup!!)
                    spotlightChipGroup!!.addView(chip)
                    addChipToStorage(word, SPOTLIGHT_CHIP)
                } else {
                    Toast.makeText(
                        this.requireContext(),
                        resources.getString(R.string.remove_chip_from_hide_group_first),
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(
                    this.requireContext(),
                    resources.getString(R.string.spotlight_chip_already_exists_message),
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            Toast.makeText(
                this.requireContext(),
                resources.getString(R.string.invalid_chip_word),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    /**
     * Creates and adds a new chip to the hide chip group.
     * Adds chip data to database and internal storage.
     *
     * @param word The word to be included in the chip.
     * @param chipGroup The chip group to put the chip in.
     */
    private fun addHideChip(word: String) {
        if (word != Constants.EMPTY_STRING) {
            // Check that the chip is not already in the hide group
            if (!checkIfChipExistsInGroup(word, hideChipGroup!!)) {
                // Check that the chip is not already in the spotlight group
                if (!checkIfChipExistsInGroup(word, spotlightChipGroup!!)) {
                    var chip: Chip = createNewChip(word, HIDE_CHIP, hideChipGroup!!)
                    hideChipGroup!!.addView(chip)
                    addChipToStorage(word, HIDE_CHIP)
                } else {
                    Toast.makeText(
                        this.requireContext(),
                        resources.getString(R.string.remove_chip_from_spotlight_group_first),
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(
                    this.requireContext(),
                    resources.getString(R.string.hide_chip_already_exists_message),
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            Toast.makeText(
                this.requireContext(),
                resources.getString(R.string.invalid_chip_word),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    /**
     * Adds a chip to the user's FireStore document and internal storage.
     *
     * @param word The word to be included in the chip.
     * @param chipType The type of chip.
     */
    private fun addChipToStorage(word: String, chipType: Int) {
        addChipToFireStore(word, chipType)
        addChipToInternalStorage(word, chipType)
    }

    /**
     * Adds a chip to the user's FireStore document if they are signed in.
     *
     * @param word The word to be included in the chip.
     * @param chipType The type of chip.
     */
    private fun addChipToFireStore(word: String, chipType: Int) {
        if (mAuth.currentUser != null) {
            if (chipType == SPOTLIGHT_CHIP) {
                UserQueryEngine(db).addSpotlightWord(word, mAuth.uid.toString())
            } else {
                UserQueryEngine(db).addHideWord(word, mAuth.uid.toString())
            }
        }
    }

    //TODO - Implement
    /**
     * Adds a chip to internal storage.
     *
     * @param word The word to be included in the chip.
     * @param chipType The type of chip.
     */
    private fun addChipToInternalStorage(word: String, chipType: Int) {

    }

    /**
     * Removes the chip record from FireStore and internal storage.
     *
     * @param chip The chip to remove.
     * @param chipType The type of the chip.
     */
    private fun removeChipFromStorage(chip: Chip, chipType: Int) {
        removeChipFromFireStore(chip, chipType)
        removeChipFromInternalStorage(chip, chipType)
    }

    /**
     * Removes the chip record from FireStore if the user is logged in.
     *
     * @param chip The chip to remove.
     * @param chipType The type of the chip.
     */
    private fun removeChipFromFireStore(chip: Chip, chipType: Int) {
        if (mAuth.currentUser != null) {
            if (chipType == SPOTLIGHT_CHIP) {
                UserQueryEngine(db).removeSpotlightWord(chip.text.toString(), mAuth.uid.toString())
            } else {
                UserQueryEngine(db).removeHideWord(chip.text.toString(), mAuth.uid.toString())
            }
        }
    }

    //TODO - Implement
    /**
     * Removes the chip record from internal storage.
     *
     * @param chip The chip to remove.
     * @param chipType The type of the chip.
     */
    private fun removeChipFromInternalStorage(chip: Chip, chipType: Int) {

    }

    /**
     * Removes all chips from storage.
     *
     * @param chipType The type of chips to remove.
     */
    private fun removeAllChipsFromStorage(chipType: Int) {
        removeAllChipsFromFireStore(chipType)
        removeAllChipsFromInternalStorage(chipType)
    }

    /**
     * Removes all chips from the user's FireStore document if they are signed in.
     *
     * @param chipType The type of chips to remove.
     */
    private fun removeAllChipsFromFireStore(chipType: Int) {
        if (mAuth.currentUser != null) {
            if (chipType == SPOTLIGHT_CHIP) {
                UserQueryEngine(db).removeAllSpotlightWords(mAuth.uid.toString())
            } else {
                UserQueryEngine(db).removeAllHideWords(mAuth.uid.toString())
            }
        }
    }

    //TODO - Implement
    /**
     * Removes all chips from internal storage.
     *
     * @param chipType The type of chips to remove.
     */
    private fun removeAllChipsFromInternalStorage(chipType: Int) {

    }

    /**
     * Stores the newly check changed chip preference in the user's FireStore document if they are
     * logged in and in internal storage.
     *
     * @param chip The chip which has been check changed.
     * @param chipType The type of chip.
     */
    private fun storeChipCheckedChange(chip: Chip, chipType: Int) {
        if (chip.isChecked) {
            if (mAuth.currentUser != null) {
                enableChipInFirestore(chip, chipType)
            }
            enableChipInInternalStorage(chip, chipType)
        } else {
            if (mAuth.currentUser != null) {
                disableChipInFirestore(chip, chipType)
            }
            disableChipInInternalStorage(chip, chipType)
        }
    }

    /**
     * Enables a chip in the user's Fire Store document.
     *
     * @param chip The chip which has been check changed.
     * @param chipType The type of chip.
     */
    private fun enableChipInFirestore(chip: Chip, chipType: Int) {
        if (chipType == SPOTLIGHT_CHIP) {
            UserQueryEngine(db).enableSpotlightWord(chip.text.toString(), mAuth.uid.toString())// TODO - Null check
        } else {
            UserQueryEngine(db).enableHideWord(chip.text.toString(), mAuth.uid.toString())// TODO - Null check
        }
    }

    /**
     * Disables a chip in the user's Fire Store document.
     *
     * @param chip The chip which has been check changed.
     * @param chipType The type of chip.
     */
    private fun disableChipInFirestore(chip: Chip, chipType: Int) {
        if (mAuth.currentUser != null) {
            if (chipType == SPOTLIGHT_CHIP) {
                UserQueryEngine(db).disableSpotlightWord(chip.text.toString(), mAuth.uid.toString())// TODO - Null check
            } else {
                UserQueryEngine(db).disableHideWord(chip.text.toString(), mAuth.uid.toString())// TODO - Null check
            }
        }
    }

    //TODO - Implement
    /**
     * Enables a chip in internal storage.
     *
     * @param chip The chip which has been check changed.
     * @param chipType The type of chip.
     */
    private fun enableChipInInternalStorage(chip: Chip, chipType: Int) {

    }

    //TODO - Implement
    /**
     * Disables a chip in internal storage.
     *
     * @param chip The chip which has been check changed.
     * @param chipType The type of chip.
     */
    private fun disableChipInInternalStorage(chip: Chip, chipType: Int) {

    }
}