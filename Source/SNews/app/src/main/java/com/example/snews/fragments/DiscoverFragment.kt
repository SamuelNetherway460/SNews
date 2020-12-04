package com.example.snews.fragments

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.example.snews.R
import com.example.snews.services.FetchArticleService
import com.example.snews.utilities.database.queryEngines.UserQueryEngine
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONArray
import org.json.JSONObject

//TODO - Populate internal storage with firestore records when a new user signs in
//TODO - Full XML Check
//TODO - Check over xml, put correct colors, i.e. iconColor
//TODO - Add sign in check
//TODO - Check null safety
//TODO - Set default discover options if not signed in
//TODO - Override current discover preferences in a user logs in
//TODO - Refresh articles in user changes a discover preference
//TODO - Run service if discover page changed
/**
 * A fragment which provides functionality for the Discover screen of the app. The discover screen
 * allows the user to customise their experience by selecting certain news related criteria.
 *
 * @property mAuth The Firebase authentication instance used to sync user discover preferences with
 *                 the FireStore database.
 * @property db    Firestore instance.
 * @author Samuel Netherway
 */
class DiscoverFragment(private val mAuth: FirebaseAuth, private val db: FirebaseFirestore) : Fragment() {

    private val DISCOVER_PREFERENCES_FILENAME = "discoverPreferences"
    private val CHANGED = true
    private val NOT_CHANGED = false
    private val RESET = false
    private var categorySwitches = ArrayList<SwitchCompat>()
    private var publisherSwitches = ArrayList<SwitchCompat>()
    private var preferenceChanged = NOT_CHANGED

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
        return inflater.inflate(R.layout.discover_fragment, container, false)
    }

    /**
     * Initialising category and publishers switches along with other UI elements.
     *
     * @param view The view hierarchy associated with the fragment.
     * @param savedInstanceState The saved state of the fragment.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setting on click listeners for all category switches
        // Business
        var businessSwitch = view.findViewById<SwitchCompat>(R.id.businessSwitch)
        businessSwitch.setOnClickListener {
            updateUserCategoryPreference(businessSwitch.isChecked, businessSwitch.text.toString()) }
        categorySwitches.add(businessSwitch)

        // Entertainment
        var entertainmentSwitch = view.findViewById<SwitchCompat>(R.id.entertainmentSwitch)
        entertainmentSwitch.setOnClickListener {
            updateUserCategoryPreference(entertainmentSwitch.isChecked, entertainmentSwitch.text.toString()) }
        categorySwitches.add(entertainmentSwitch)

        // General
        var generalSwitch = view.findViewById<SwitchCompat>(R.id.generalSwitch)
        generalSwitch.setOnClickListener {
            updateUserCategoryPreference(generalSwitch.isChecked, generalSwitch.text.toString()) }
        categorySwitches.add(generalSwitch)

        // Health
        var healthSwitch = view.findViewById<SwitchCompat>(R.id.healthSwitch)
        healthSwitch.setOnClickListener {
            updateUserCategoryPreference(healthSwitch.isChecked, healthSwitch.text.toString()) }
        categorySwitches.add(healthSwitch)

        // Science
        var scienceSwitch = view.findViewById<SwitchCompat>(R.id.scienceSwitch)
        scienceSwitch.setOnClickListener {
            updateUserCategoryPreference(scienceSwitch.isChecked, scienceSwitch.text.toString()) }
        categorySwitches.add(scienceSwitch)

        // Sports
        var sportsSwitch = view.findViewById<SwitchCompat>(R.id.sportsSwitch)
        sportsSwitch.setOnClickListener {
            updateUserCategoryPreference(sportsSwitch.isChecked, sportsSwitch.text.toString()) }
        categorySwitches.add(sportsSwitch)

        // Technology
        var technologySwitch = view.findViewById<SwitchCompat>(R.id.technologySwitch)
        technologySwitch.setOnClickListener {
            updateUserCategoryPreference(technologySwitch.isChecked, technologySwitch.text.toString()) }
        categorySwitches.add(technologySwitch)


        // Setting on click listeners for all publisher switches
        // Wired
        var wiredSwitch = view.findViewById<SwitchCompat>(R.id.wiredSwitch)
        wiredSwitch.setOnClickListener {
            updateUserPublisherPreference(wiredSwitch.isChecked, wiredSwitch.text.toString()) }
        publisherSwitches.add(wiredSwitch)

        // Tech Crunch
        var techCrunchSwitch = view.findViewById<SwitchCompat>(R.id.techCrunchSwitch)
        techCrunchSwitch.setOnClickListener {
            updateUserPublisherPreference(techCrunchSwitch.isChecked, techCrunchSwitch.text.toString()) }
        publisherSwitches.add(techCrunchSwitch)

        // The Next Web
        var theNextWebSwitch = view.findViewById<SwitchCompat>(R.id.theNextWebSwitch)
        theNextWebSwitch.setOnClickListener {
            updateUserPublisherPreference(theNextWebSwitch.isChecked, theNextWebSwitch.text.toString()) }
        publisherSwitches.add(theNextWebSwitch)

        // BBC News
        var bbcNewsSwitch = view.findViewById<SwitchCompat>(R.id.bbcNewsSwitch)
        bbcNewsSwitch.setOnClickListener {
            updateUserPublisherPreference(bbcNewsSwitch.isChecked, bbcNewsSwitch.text.toString()) }
        publisherSwitches.add(bbcNewsSwitch)

        // Mac Rumors
        var macRumorsSwitch = view.findViewById<SwitchCompat>(R.id.macRumorsSwitch)
        macRumorsSwitch.setOnClickListener {
            updateUserPublisherPreference(macRumorsSwitch.isChecked, macRumorsSwitch.text.toString()) }
        publisherSwitches.add(macRumorsSwitch)

        // CNET
        var cnetSwitch = view.findViewById<SwitchCompat>(R.id.cnetSwitch)
        cnetSwitch.setOnClickListener {
            updateUserPublisherPreference(cnetSwitch.isChecked, cnetSwitch.text.toString()) }
        publisherSwitches.add(cnetSwitch)

        // Bleacher Report
        var bleacherReportSwitch = view.findViewById<SwitchCompat>(R.id.bleacherReportSwitch)
        bleacherReportSwitch.setOnClickListener {
            updateUserPublisherPreference(bleacherReportSwitch.isChecked, bleacherReportSwitch.text.toString()) }
        publisherSwitches.add(bleacherReportSwitch)

        // Bloomberg
        var bloombergSwitch = view.findViewById<SwitchCompat>(R.id.bloombergSwitch)
        bloombergSwitch.setOnClickListener {
            updateUserPublisherPreference(bloombergSwitch.isChecked, bloombergSwitch.text.toString()) }
        publisherSwitches.add(bloombergSwitch)

        // Business Insider
        var businessInsiderSwitch = view.findViewById<SwitchCompat>(R.id.businessInsiderSwitch)
        businessInsiderSwitch.setOnClickListener {
            updateUserPublisherPreference(businessInsiderSwitch.isChecked, businessInsiderSwitch.text.toString()) }
        publisherSwitches.add(businessInsiderSwitch)

        // Buzzfeed
        var buzzfeedSwitch = view.findViewById<SwitchCompat>(R.id.buzzfeedSwitch)
        buzzfeedSwitch.setOnClickListener {
            updateUserPublisherPreference(buzzfeedSwitch.isChecked, buzzfeedSwitch.text.toString()) }
        publisherSwitches.add(buzzfeedSwitch)

        // CNN
        var cnnSwitch = view.findViewById<SwitchCompat>(R.id.cnnSwitch)
        cnnSwitch.setOnClickListener {
            updateUserPublisherPreference(cnnSwitch.isChecked, cnnSwitch.text.toString()) }
        publisherSwitches.add(cnnSwitch)

        // ESPN
        var espnSwitch = view.findViewById<SwitchCompat>(R.id.espnSwitch)
        espnSwitch.setOnClickListener {
            updateUserPublisherPreference(espnSwitch.isChecked, espnSwitch.text.toString()) }
        publisherSwitches.add(espnSwitch)

        // Financial Post
        var finantialPostSwitch = view.findViewById<SwitchCompat>(R.id.finantialPostSwitch)
        finantialPostSwitch.setOnClickListener {
            updateUserPublisherPreference(finantialPostSwitch.isChecked, finantialPostSwitch.text.toString()) }
        publisherSwitches.add(finantialPostSwitch)

        // Fox News
        var foxNewsSwitch = view.findViewById<SwitchCompat>(R.id.foxNewsSwitch)
        foxNewsSwitch.setOnClickListener {
            updateUserPublisherPreference(foxNewsSwitch.isChecked, foxNewsSwitch.text.toString()) }
        publisherSwitches.add(foxNewsSwitch)

        updateCategories()
        updatePublishers()
    }

    //TODO - Documentation
    /**
     *
     */
    override fun onPause() {
        super.onPause()
        Log.d(ContentValues.TAG, "DISCOVER FRAGMENT - ON PAUSE CALLED")
    }

    //TODO - Implement or remove
    /**
     *
     */
    override fun onResume() {
        super.onResume()
        Log.d(ContentValues.TAG, "DISCOVER FRAGMENT - ON RESUME CALLED")
    }

    /**
     * Refresh article store when exiting the discover fragment to reflect the user's new discover
     * preferences.
     */
    override fun onStop() {
        super.onStop()
        Log.d(ContentValues.TAG, "DISCOVER FRAGMENT - ON STOP CALLED")
        if (preferenceChanged) {
            Log.d(ContentValues.TAG, "DISCOVER FRAGMENT - PREFERENCE CHANGED")
            context!!.startService(Intent(context, FetchArticleService::class.java))
            preferenceChanged = RESET
        }
    }

    //TODO - Implement or remove
    /**
     *
     */
    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(ContentValues.TAG, "DISCOVER FRAGMENT - ON DESTROY VIEW CALLED")
    }

    /**
     * Update the category UI selections using the user's account or internal storage if not
     * signed in.
     */
    private fun updateCategories() {
        // First try FireStore
        var uid: String? = mAuth.uid
        if (uid != null) {
            val user = db.collection("users").document(uid)
            user.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            updateUICategories(document.get("categories") as ArrayList<String>)
                        }
                    }
        // Use internal storage copy if FireStore is unavailable
        } else {
            updateUICategories(getUserCategoryPreferences())
        }
    }

    /**
     * Updates the user interface with the category preferences for the currently signed in user.
     *
     * @param selectedCategories A list of selected categories for the currently signed in user.
     */
    private fun updateUICategories(selectedCategories: ArrayList<String>) {
        for (switch in categorySwitches) {
            switch.isChecked = selectedCategories.contains(switch.text.toString())
        }
    }

    /**
     * Stores the user's new category preference to their account and local internal storage.
     *
     * @param isChecked A boolean indicating whether the category is selected or not.
     * @param category The category preference which has been altered.
     */
    private fun updateUserCategoryPreference(isChecked: Boolean, category: String) {
        updateDatabaseCategory(isChecked, category)
        updateInternalStorageCategory(isChecked, category)
        preferenceChanged = CHANGED
    }

    /**
     * Updates the FireStore database with the newly changed category preference.
     *
     * @param isChecked A boolean indicating whether the category is selected or not.
     * @param category The category preference which has been altered.
     */
    private fun updateDatabaseCategory(isChecked: Boolean, category: String) {
        if (mAuth.uid != null) {
            var userQuery = UserQueryEngine(db)
            if (isChecked) {
                userQuery.addCategory(category, mAuth.uid!!)
            } else {
                userQuery.removeCategory(category, mAuth.uid!!)
            }
        }
    }

    /**
     * Updates the internal storage discovery preferences file with the updated category preference.
     *
     * @param isChecked A boolean indicating whether the category is selected or not.
     * @param category The category preference which has been altered.
     */
    private fun updateInternalStorageCategory(isChecked: Boolean, category: String) {
        var discoverPreferences = JSONObject(readDiscoverPreferences())
        // Add the newly selected category
        if (isChecked) {
            discoverPreferences.getJSONArray("categories").put(category)
        // Find and remove the newly selected category
        } else {
            var oldCategories = discoverPreferences.getJSONArray("categories")
            var newCategories = JSONArray()
            for (i in 0..oldCategories.length() - 1) {
                if (oldCategories.get(i) != category) newCategories.put(oldCategories.get(i))
            }
            discoverPreferences.put("categories", newCategories)
        }
        writeDiscoverPreferences(discoverPreferences.toString())
    }


    /**
     * Reads the user's discover preferences from internal storage.
     *
     * @return A string JSON containing the user's discover preferences.
     */
    private fun readDiscoverPreferences() : String {
        activity!!.openFileInput(DISCOVER_PREFERENCES_FILENAME).use {
            return it.readBytes().decodeToString()
        }
    }

    /**
     * Write the user's discover preferences to internal storage.
     *
     * @param preferences a string JSON containing the user's discover preferences.
     */
    private fun writeDiscoverPreferences(preferences: String) {
        activity!!.openFileOutput(DISCOVER_PREFERENCES_FILENAME, Context.MODE_PRIVATE).use {
            it.write(preferences.toByteArray())
        }
    }

    /**
     * Update the publisher UI selections using the user's account or internal storage if not
     * signed in.
     */
    private fun updatePublishers() {
        // First try FireStore preferences
        var uid: String? = mAuth.uid
        if (uid != null) {
            val user = db.collection("users").document(uid)
            user.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            updateUIPublishers(document.get("publishers") as ArrayList<String>)
                        }
                    }
        // Use internal storage copy if FireStore is unavailable
        } else {
            updateUIPublishers(getUserPublisherPreferences())
        }
    }

    /**
     * Updates the user interface with the publisher preferences for the currently signed in user.
     *
     * @param selectedPublishers A list of selected publishers for the currently signed in user.
     */
    private fun updateUIPublishers(selectedPublishers: ArrayList<String>) {
        for (switch in publisherSwitches) {
            switch.isChecked = selectedPublishers.contains(switch.text.toString())
        }
    }

    /**
     * Stores the user's new publisher preference to their account and local internal storage.
     *
     * @param isChecked A boolean indicating whether the publisher is selected or not.
     * @param publisher The publisher preference which has been altered.
     */
    private fun updateUserPublisherPreference(isChecked: Boolean, publisher: String) {
        updateDatabasePublisher(isChecked, publisher)
        updateInternalStoragePublisher(isChecked, publisher)
        preferenceChanged = CHANGED
    }

    /**
     * Updates the FireStore database with the newly changed publisher preference.
     *
     * @param isChecked A boolean indicating whether the publisher is selected or not.
     * @param publisher The publisher preference which has been altered.
     */
    private fun updateDatabasePublisher(isChecked: Boolean, publisher: String) {
        if (mAuth.uid != null) {
            var userQuery = UserQueryEngine(db)
            if (isChecked) {
                userQuery.addPublisher(publisher, mAuth.uid!!)
            } else {
                userQuery.removePublisher(publisher, mAuth.uid!!)
            }
        }
    }

    /**
     * Updates the internal storage discovery preferences file with the updated publisher preference.
     *
     * @param isChecked A boolean indicating whether the publisher is selected or not.
     * @param publisher The publisher preference which has been altered.
     */
    private fun updateInternalStoragePublisher(isChecked: Boolean, publisher: String) {
        var discoverPreferences = JSONObject(readDiscoverPreferences())
        // Add the newly selected publisher
        if (isChecked) {
            discoverPreferences.getJSONArray("publishers").put(publisher)
            // Find and remove the newly selected publisher
        } else {
            var oldPublishers = discoverPreferences.getJSONArray("publishers")
            var newPublishers = JSONArray()
            for (i in 0..oldPublishers.length() - 1) {
                if (oldPublishers.get(i) != publisher) newPublishers.put(oldPublishers.get(i))
            }
            discoverPreferences.put("publishers", newPublishers)
        }
        writeDiscoverPreferences(discoverPreferences.toString())
    }

    /**
     * Gets the user's selected discover categories.
     *
     * @return An array list containing the user's selected discover categories.
     */
    private fun getUserCategoryPreferences() : ArrayList<String> {
        var jsonCategories = JSONObject(readDiscoverPreferences()).getJSONArray("categories") //TODO - categories final variable
        var selectedCategoriesList = ArrayList<String>()
        for (i in 0..jsonCategories.length() - 1) {
            selectedCategoriesList.add(jsonCategories.getString(i))
        }
        return selectedCategoriesList
    }

    /**
     * Gets the user's selected discover publishers.
     *
     * @return An array list containing the user's selected discover publishers.
     */
    private fun getUserPublisherPreferences() : ArrayList<String> {
        var jsonPublishers = JSONObject(readDiscoverPreferences()).getJSONArray("publishers") //TODO - publishers final variable
        var selectedPublishersList = ArrayList<String>()
        for (i in 0..jsonPublishers.length() - 1) {
            selectedPublishersList.add(jsonPublishers.getString(i))
        }
        return selectedPublishersList
    }
}