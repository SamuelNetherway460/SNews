package com.example.snews.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.example.snews.R
import com.example.snews.services.FetchArticleService
import com.example.snews.utilities.Constants
import com.example.snews.utilities.database.UserQueryEngine
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

//TODO - Upate UI categories based off shared preferences
//TODO - In the relevant on method, check that the current chip groups are correct based off the users account
//TODO - Full XML Check
//TODO - Implement shared preferences for storage of device specific preferences
//TODO - Shared preferences for notifications being on or off
/**
 * Fragment responsible for managing the user account and app preferences.
 *
 * @author Samuel Netherway
 */
class ProfileFragment(private val mAuth: FirebaseAuth, private val db: FirebaseFirestore) : Fragment() {

    companion object {
        const val SPOTLIGHT_CHIP = 0
        const val HIDE_CHIP = 1
        const val DEFAULT_FETCH_ARTICLES_HOUR = 21
        const val DEFAULT_FETCH_ARTICLES_MINUTE = 0
    }

    private var spotlightChipGroup: ChipGroup? = null
    private var hideChipGroup: ChipGroup? = null
    private var sharedPreferences: SharedPreferences? = null
    private var notificationSwitches = ArrayList<SwitchCompat>()


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
        activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        sharedPreferences = context!!.getSharedPreferences(Constants.SHARED_PREFERENCES_FILENAME, 0)

        // General
        val signInRegister = view.findViewById<Button>(R.id.signInRegisterButton)
        val loggedInStatus = view.findViewById<TextView>(R.id.loggedInStatus)
        val fetchArticlesTimePicker = view.findViewById<TimePicker>(R.id.fetchArticlesTimePicker)

        // Spotlight filtering
        val spotlightWordEditText = view.findViewById<EditText>(R.id.spotlightWordEditText)
        val addSpotlightChipButton = view.findViewById<Button>(R.id.addSpotlightChip)
        val spotlightRemoveAll = view.findViewById<Button>(R.id.spotlightRemoveAll)
        spotlightChipGroup = view.findViewById<ChipGroup>(R.id.spotlightChipGroup)

        // Hide filtering
        val addHideChipButton = view.findViewById<Button>(R.id.addHideChip)
        val hideWordEditText = view.findViewById<EditText>(R.id.hideWordEditText)
        val hideRemoveAll = view.findViewById<Button>(R.id.hideRemoveAll)
        hideChipGroup = view.findViewById<ChipGroup>(R.id.hideChipGroup)

        // Category notification preferences
        val businessNotificationSwitch = view.findViewById<SwitchCompat>(R.id.businessNotificationSwitch)
        notificationSwitches.add(businessNotificationSwitch)
        val entertainmentNotificationSwitch = view.findViewById<SwitchCompat>(R.id.entertainmentNotificationSwitch)
        notificationSwitches.add(entertainmentNotificationSwitch)
        val generalNotificationSwitch = view.findViewById<SwitchCompat>(R.id.generalNotificationSwitch)
        notificationSwitches.add(generalNotificationSwitch)
        val healthNotificationSwitch = view.findViewById<SwitchCompat>(R.id.healthNotificationSwitch)
        notificationSwitches.add(healthNotificationSwitch)
        val scienceNotificationSwitch = view.findViewById<SwitchCompat>(R.id.scienceNotificationSwitch)
        notificationSwitches.add(scienceNotificationSwitch)
        val sportsNotificationSwitch = view.findViewById<SwitchCompat>(R.id.sportsNotificationSwitch)
        notificationSwitches.add(sportsNotificationSwitch)
        val technologyNotificationSwitch = view.findViewById<SwitchCompat>(R.id.technologyNotificationSwitch)
        notificationSwitches.add(technologyNotificationSwitch)

        // Display the correct message depending on if the user is signed in or not
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

        // Set time to fetch articles
        fetchArticlesTimePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
            addArticleFetchTimeToSharedPreferences(hourOfDay, minute)
            updateAlarmManager()
        }

        // Business notifications
        businessNotificationSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                turnCategoryNotificationsOn(businessNotificationSwitch.text.toString())
            } else {
                turnCategoryNotificationsOff(businessNotificationSwitch.text.toString())
            }
        }

        // Entertainment notifications
        entertainmentNotificationSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                turnCategoryNotificationsOn(entertainmentNotificationSwitch.text.toString())
            } else {
                turnCategoryNotificationsOff(entertainmentNotificationSwitch.text.toString())
            }
        }

        // General notifications
        generalNotificationSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                turnCategoryNotificationsOn(generalNotificationSwitch.text.toString())
            } else {
                turnCategoryNotificationsOff(generalNotificationSwitch.text.toString())
            }
        }

        // Health notifications
        healthNotificationSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                turnCategoryNotificationsOn(healthNotificationSwitch.text.toString())
            } else {
                turnCategoryNotificationsOff(healthNotificationSwitch.text.toString())
            }
        }

        // Science notifications
        scienceNotificationSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                turnCategoryNotificationsOn(scienceNotificationSwitch.text.toString())
            } else {
                turnCategoryNotificationsOff(scienceNotificationSwitch.text.toString())
            }
        }

        // Sports notifications
        sportsNotificationSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                turnCategoryNotificationsOn(sportsNotificationSwitch.text.toString())
            } else {
                turnCategoryNotificationsOff(sportsNotificationSwitch.text.toString())
            }
        }

        // Technology notifications
        technologyNotificationSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                turnCategoryNotificationsOn(technologyNotificationSwitch.text.toString())
            } else {
                turnCategoryNotificationsOff(technologyNotificationSwitch.text.toString())
            }
        }

        updateNotificationSwitches()
    }

    /**
     * Updates the user interface for the notification switches.
     */
    private fun updateNotificationSwitches() {
        for (switch in notificationSwitches) {
            var isChecked = sharedPreferences!!.getBoolean(switch.text.toString().toLowerCase(), false)
            switch.isChecked = isChecked
        }
    }

    /**
     * Turns on notifications for a category in shared preferences.
     *
     * @param category The category to receive notifications about.
     */
    private fun turnCategoryNotificationsOn(category: String) {
        var editor = sharedPreferences!!.edit()
        editor.putBoolean(category.toLowerCase(), true)
        editor.commit()
    }

    /**
     * Turns off notifications for a category in shared preferences.
     *
     * @param category The category to not receive notifications about.
     */
    private fun turnCategoryNotificationsOff(category: String) {
        var editor = sharedPreferences!!.edit()
        editor.putBoolean(category.toLowerCase(), false)
        editor.commit()
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
            updateChipsUI()
        } else {
            if (signInRegister != null) signInRegister.setText(resources.getString(R.string.sign_in_register))
            updateChipsUI()
        }
    }

    /**
     * Adds the user selected article fetch time.
     *
     * @param hourOfDay The hour to fetch the articles.
     * @param minute The minute to fetch the articles.
     */
    private fun addArticleFetchTimeToSharedPreferences(hourOfDay: Int, minute: Int) {
        var editor = sharedPreferences!!.edit()
        editor.putInt(Constants.FETCH_ARTICLES_HOUR_FIELD_NAME, hourOfDay)
        editor.putInt(Constants.FETCH_ARTICLES_MINUTE_FIELD_NAME, minute)
        editor.commit()
    }

    /**
     * Updates the alarm manager to fetch articles every day at the user specified time.
     */
    private fun updateAlarmManager() {
        var hour = sharedPreferences!!.getInt(
                Constants.FETCH_ARTICLES_HOUR_FIELD_NAME, Constants.DEFAULT_FETCH_ARTICLES_HOUR)
        var minute = sharedPreferences!!.getInt(
                Constants.FETCH_ARTICLES_MINUTE_FIELD_NAME, Constants.DEFAULT_FETCH_ARTICLES_MINUTE)
        var calender = Calendar.getInstance()
        calender.set(Calendar.HOUR_OF_DAY, hour)
        calender.set(Calendar.MINUTE, minute)
        var daily = 24 * 60 * 60 * 1000
        var milliseconds = calender.timeInMillis
        // If time has already passed, add a day
        if (milliseconds < System.currentTimeMillis()) milliseconds += daily
        val intent = Intent(context, FetchArticleService::class.java)
        // FLAG to avoid creating another service if there is already one
        val pendingIntent = PendingIntent.getService(context, 1, intent,
                PendingIntent.FLAG_CANCEL_CURRENT)
        val alarmManager = context!!.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, milliseconds, AlarmManager.INTERVAL_DAY,
                pendingIntent)
    }

    /**
     * Updates the user interface to represent the user's key word filtering preferences.
     */
    private fun updateChipsUI() {
        spotlightChipGroup!!.removeAllViews()
        hideChipGroup!!.removeAllViews()
        updateSpotlightChipsUI()
        updateHideChipsUI()
    }

    /**
     * Updates the user interface to represent the user's spotlight word preferences.
     */
    private fun updateSpotlightChipsUI() {
        var internalPreferences = JSONObject(readInternalPreferences())
        var chipJSONS = jsonArrayToArrayList(internalPreferences.getJSONArray(Constants.INTERNAL_SPOTLIGHT_JSON_ARRAY_NAME))

        for (chip in chipJSONS) {
            var word = chip.getString(Constants.INTERNAL_WORD_JSON_NAME)
            var enabled = chip.getBoolean(Constants.INTERNAL_ENABLED_JSON_NAME)
            var newChip = createNewChip(word, SPOTLIGHT_CHIP, spotlightChipGroup!!)
            if (!enabled) newChip.isChecked = false
            spotlightChipGroup!!.addView(newChip)
        }
    }

    /**
     * Updates the user interface to represent the user's hide word preferences.
     */
    private fun updateHideChipsUI() {
        var internalPreferences = JSONObject(readInternalPreferences())
        var chipJSONS = jsonArrayToArrayList(internalPreferences.getJSONArray(Constants.INTERNAL_HIDE_JSON_ARRAY_NAME))

        for (chip in chipJSONS) {
            var word = chip.getString(Constants.INTERNAL_WORD_JSON_NAME)
            var enabled = chip.getBoolean(Constants.INTERNAL_ENABLED_JSON_NAME)
            var newChip = createNewChip(word, HIDE_CHIP, hideChipGroup!!)
            if (!enabled) newChip.isChecked = false
            hideChipGroup!!.addView(newChip)
        }
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
                Constants.SIGN_IN_REGISTER_FRAGMENT_TAG
        )
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

    /**
     * Adds a chip to internal storage.
     *
     * @param word The word to be included in the chip.
     * @param chipType The type of chip.
     */
    private fun addChipToInternalStorage(word: String, chipType: Int) {
        var chipJSON = JSONObject()
        chipJSON.put(Constants.INTERNAL_WORD_JSON_NAME, word)
        chipJSON.put(Constants.INTERNAL_ENABLED_JSON_NAME, true)

        var internalPreferences = JSONObject(readInternalPreferences())
        var chips: JSONArray

        if (chipType == SPOTLIGHT_CHIP) {
            chips = internalPreferences.getJSONArray(Constants.INTERNAL_SPOTLIGHT_JSON_ARRAY_NAME)
            chips.put(chipJSON)
            internalPreferences.put(Constants.INTERNAL_SPOTLIGHT_JSON_ARRAY_NAME, chips)
        } else {
            chips = internalPreferences.getJSONArray(Constants.INTERNAL_HIDE_JSON_ARRAY_NAME)
            chips.put(chipJSON)
            internalPreferences.put(Constants.INTERNAL_HIDE_JSON_ARRAY_NAME, chips)
        }

        writeToInternalStorage(internalPreferences.toString())
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

    /**
     * Removes the chip record from internal storage.
     *
     * @param chip The chip to remove.
     * @param chipType The type of the chip.
     */
    private fun removeChipFromInternalStorage(chip: Chip, chipType: Int) {
        var word = chip.text.toString()

        var internalPreferences = JSONObject(readInternalPreferences())
        var chips: ArrayList<JSONObject>
        var newChips = ArrayList<JSONObject>()

        if (chipType == SPOTLIGHT_CHIP) {
            chips = jsonArrayToArrayList(internalPreferences.getJSONArray(Constants.INTERNAL_SPOTLIGHT_JSON_ARRAY_NAME))
        } else {
            chips = jsonArrayToArrayList(internalPreferences.getJSONArray(Constants.INTERNAL_HIDE_JSON_ARRAY_NAME))
        }

        for (chip in chips) {
            var currentChipWord = chip.getString(Constants.INTERNAL_WORD_JSON_NAME)
            if (currentChipWord != word) newChips.add(chip)
        }

        if (chipType == SPOTLIGHT_CHIP) {
            internalPreferences.put(Constants.INTERNAL_SPOTLIGHT_JSON_ARRAY_NAME, jsonArrayListToJSONArray(newChips))
        } else {
            internalPreferences.put(Constants.INTERNAL_HIDE_JSON_ARRAY_NAME, jsonArrayListToJSONArray(newChips))
        }
        writeToInternalStorage(internalPreferences.toString())
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

    /**
     * Removes all chips from internal storage.
     *
     * @param chipType The type of chips to remove.
     */
    private fun removeAllChipsFromInternalStorage(chipType: Int) {
        var internalPreferences = JSONObject(readInternalPreferences())
        if (chipType == SPOTLIGHT_CHIP) {
            internalPreferences.put(Constants.INTERNAL_SPOTLIGHT_JSON_ARRAY_NAME, JSONArray())
        } else {
            internalPreferences.put(Constants.INTERNAL_HIDE_JSON_ARRAY_NAME, JSONArray())
        }
        writeToInternalStorage(internalPreferences.toString())
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
            UserQueryEngine(db).enableSpotlightWord(chip.text.toString(), mAuth.uid.toString())
        } else {
            UserQueryEngine(db).enableHideWord(chip.text.toString(), mAuth.uid.toString())
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
                UserQueryEngine(db).disableSpotlightWord(chip.text.toString(), mAuth.uid.toString())
            } else {
                UserQueryEngine(db).disableHideWord(chip.text.toString(), mAuth.uid.toString())
            }
        }
    }

    /**
     * Enables a chip in internal storage.
     *
     * @param chip The chip which has been check changed.
     * @param chipType The type of chip.
     */
    private fun enableChipInInternalStorage(chip: Chip, chipType: Int) {
        var word = chip.text.toString()

        var internalPreferences = JSONObject(readInternalPreferences())
        var chips: ArrayList<JSONObject>

        if (chipType == SPOTLIGHT_CHIP) {
            chips = jsonArrayToArrayList(internalPreferences.getJSONArray(Constants.INTERNAL_SPOTLIGHT_JSON_ARRAY_NAME))
        } else {
            chips = jsonArrayToArrayList(internalPreferences.getJSONArray(Constants.INTERNAL_HIDE_JSON_ARRAY_NAME))
        }

        // Find and enable the word
        for (chip in chips) {
            if (chip.getString(Constants.INTERNAL_WORD_JSON_NAME) == word) {
                chip.put(Constants.INTERNAL_ENABLED_JSON_NAME, true)
            }
        }

        if (chipType == SPOTLIGHT_CHIP) {
            internalPreferences.put(Constants.INTERNAL_SPOTLIGHT_JSON_ARRAY_NAME, jsonArrayListToJSONArray(chips))
        } else {
            internalPreferences.put(Constants.INTERNAL_HIDE_JSON_ARRAY_NAME, jsonArrayListToJSONArray(chips))
        }
        writeToInternalStorage(internalPreferences.toString())
    }

    /**
     * Disables a chip in internal storage.
     *
     * @param chip The chip which has been check changed.
     * @param chipType The type of chip.
     */
    private fun disableChipInInternalStorage(chip: Chip, chipType: Int) {
        var word = chip.text.toString()

        var internalPreferences = JSONObject(readInternalPreferences())
        var chips: ArrayList<JSONObject>

        if (chipType == SPOTLIGHT_CHIP) {
            chips = jsonArrayToArrayList(internalPreferences.getJSONArray(Constants.INTERNAL_SPOTLIGHT_JSON_ARRAY_NAME))
        } else {
            chips = jsonArrayToArrayList(internalPreferences.getJSONArray(Constants.INTERNAL_HIDE_JSON_ARRAY_NAME))
        }

        // Find and disable the word
        for (chip in chips) {
            if (chip.getString(Constants.INTERNAL_WORD_JSON_NAME) == word) {
                chip.put(Constants.INTERNAL_ENABLED_JSON_NAME, false)
            }
        }

        if (chipType == SPOTLIGHT_CHIP) {
            internalPreferences.put(Constants.INTERNAL_SPOTLIGHT_JSON_ARRAY_NAME, jsonArrayListToJSONArray(chips))
        } else {
            internalPreferences.put(Constants.INTERNAL_HIDE_JSON_ARRAY_NAME, jsonArrayListToJSONArray(chips))
        }
        writeToInternalStorage(internalPreferences.toString())
    }

    /**
     * Reads the user's preferences from the internal storage file.
     *
     * @return The user's preferences.
     */
    private fun readInternalPreferences() : String {
        activity!!.openFileInput(Constants.INTERNAL_PREFERENCES_FILENAME).use {
            return it.readBytes().decodeToString()
        }
    }

    /**
     * Writes string data to the user's preferences file in internal storage.
     *
     * @param string The string data to write to the file.
     */
    private fun writeToInternalStorage(string: String) {
        activity!!.openFileOutput(Constants.INTERNAL_PREFERENCES_FILENAME, Context.MODE_PRIVATE).use {
            it.write(string.toByteArray())
        }
    }

    //TODO - Move to utilities class
    /**
     * Converts a JSON array of JSON object to a array list of JSON objects.
     *
     * @param jsonArray The JSON array to be converted.
     * @return An array list of JSON objects.
     */
    private fun jsonArrayToArrayList(jsonArray: JSONArray) : ArrayList<JSONObject> {
        var jsonArrayList = ArrayList<JSONObject>()
        for (i in 0..jsonArray.length() - 1) {
            jsonArrayList.add(jsonArray[i] as JSONObject)
        }
        return jsonArrayList
    }

    //TODO - Move to utilities class
    /**
     * Converts an array list of JSON objects into a JSON array.
     *
     * @param jsonArrayList The array list of json objects.
     * @return A JSON array.
     */
    private fun jsonArrayListToJSONArray(jsonArrayList: ArrayList<JSONObject>) : JSONArray {
        var jsonArray = JSONArray()
        for (json in jsonArrayList) {
            jsonArray.put(json)
        }
        return jsonArray
    }
}