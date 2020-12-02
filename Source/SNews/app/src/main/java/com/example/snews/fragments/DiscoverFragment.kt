package com.example.snews.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.example.snews.R
import com.example.snews.utilities.database.queryEngines.UserQueryEngine
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

//TODO - Full XML Check
//TODO - Check over xml, put correct colors, i.e. iconColor
//TODO - Add sign in check
//TODO - Check null safety
//TODO - Set default discover options if not signed in
/**
 * A fragment which provides functionality for the Discover screen of the app. The discover screen
 * allows the user to customise their experience by selecting certain news related criteria.
 *
 * @property tAuth The Firebase authentication instance used to sync user discover preferences with
 *              the FireStore database.
 * @property db    Firestore instance.
 * @author Samuel Netherway
 */
class DiscoverFragment(private val tAuth: FirebaseAuth, private val db: FirebaseFirestore) : Fragment() {

    private var categorySwitches = ArrayList<SwitchCompat>()
    private var publisherSwitches = ArrayList<SwitchCompat>()

    //TODO - Possibly remove
    /**
     * Creates and returns the view hierarchy associated with the fragment.
     *
     * @param inflater The layout inflater associated with the fragment.
     * @param container The fragment container.
     * @param savedInstanceState The saved state of the fragment.
     * @return The view hierarchy associated with the fragment.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.discover_fragment, container, false)
    }

    //TODO - Documentation
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
        businessSwitch.setOnClickListener { updateDatabaseCategory(businessSwitch.isChecked, businessSwitch.text.toString()) }
        categorySwitches.add(businessSwitch)

        // Entertainment
        var entertainmentSwitch = view.findViewById<SwitchCompat>(R.id.entertainmentSwitch)
        entertainmentSwitch.setOnClickListener { updateDatabaseCategory(entertainmentSwitch.isChecked, entertainmentSwitch.text.toString()) }
        categorySwitches.add(entertainmentSwitch)

        // General
        var generalSwitch = view.findViewById<SwitchCompat>(R.id.generalSwitch)
        generalSwitch.setOnClickListener { updateDatabaseCategory(generalSwitch.isChecked, generalSwitch.text.toString()) }
        categorySwitches.add(generalSwitch)

        // Health
        var healthSwitch = view.findViewById<SwitchCompat>(R.id.healthSwitch)
        healthSwitch.setOnClickListener { updateDatabaseCategory(healthSwitch.isChecked, healthSwitch.text.toString()) }
        categorySwitches.add(healthSwitch)

        // Science
        var scienceSwitch = view.findViewById<SwitchCompat>(R.id.scienceSwitch)
        scienceSwitch.setOnClickListener { updateDatabaseCategory(scienceSwitch.isChecked, scienceSwitch.text.toString()) }
        categorySwitches.add(scienceSwitch)

        // Sports
        var sportsSwitch = view.findViewById<SwitchCompat>(R.id.sportsSwitch)
        sportsSwitch.setOnClickListener { updateDatabaseCategory(sportsSwitch.isChecked, sportsSwitch.text.toString()) }
        categorySwitches.add(sportsSwitch)

        // Technology
        var technologySwitch = view.findViewById<SwitchCompat>(R.id.technologySwitch)
        technologySwitch.setOnClickListener { updateDatabaseCategory(technologySwitch.isChecked, technologySwitch.text.toString()) }
        categorySwitches.add(technologySwitch)


        // Setting on click listeners for all publisher switches
        // Wired
        var wiredSwitch = view.findViewById<SwitchCompat>(R.id.wiredSwitch)
        wiredSwitch.setOnClickListener { updateDatabasePublisher(wiredSwitch.isChecked, wiredSwitch.text.toString()) }
        publisherSwitches.add(wiredSwitch)

        // Tech Crunch
        var techCrunchSwitch = view.findViewById<SwitchCompat>(R.id.techCrunchSwitch)
        techCrunchSwitch.setOnClickListener { updateDatabasePublisher(techCrunchSwitch.isChecked, techCrunchSwitch.text.toString()) }
        publisherSwitches.add(techCrunchSwitch)

        // The Next Web
        var theNextWebSwitch = view.findViewById<SwitchCompat>(R.id.theNextWebSwitch)
        theNextWebSwitch.setOnClickListener { updateDatabasePublisher(theNextWebSwitch.isChecked, theNextWebSwitch.text.toString()) }
        publisherSwitches.add(theNextWebSwitch)

        // BBC News
        var bbcNewsSwitch = view.findViewById<SwitchCompat>(R.id.bbcNewsSwitch)
        bbcNewsSwitch.setOnClickListener { updateDatabasePublisher(bbcNewsSwitch.isChecked, bbcNewsSwitch.text.toString()) }
        publisherSwitches.add(bbcNewsSwitch)

        // Mac Rumors
        var macRumorsSwitch = view.findViewById<SwitchCompat>(R.id.macRumorsSwitch)
        macRumorsSwitch.setOnClickListener { updateDatabasePublisher(macRumorsSwitch.isChecked, macRumorsSwitch.text.toString()) }
        publisherSwitches.add(macRumorsSwitch)

        // CNET
        var cnetSwitch = view.findViewById<SwitchCompat>(R.id.cnetSwitch)
        cnetSwitch.setOnClickListener { updateDatabasePublisher(cnetSwitch.isChecked, cnetSwitch.text.toString()) }
        publisherSwitches.add(cnetSwitch)

        // Bleacher Report
        var bleacherReportSwitch = view.findViewById<SwitchCompat>(R.id.bleacherReportSwitch)
        bleacherReportSwitch.setOnClickListener { updateDatabasePublisher(bleacherReportSwitch.isChecked, bleacherReportSwitch.text.toString()) }
        publisherSwitches.add(bleacherReportSwitch)

        // Bloomberg
        var bloombergSwitch = view.findViewById<SwitchCompat>(R.id.bloombergSwitch)
        bloombergSwitch.setOnClickListener { updateDatabasePublisher(bloombergSwitch.isChecked, bloombergSwitch.text.toString()) }
        publisherSwitches.add(bloombergSwitch)

        // Business Insider
        var businessInsiderSwitch = view.findViewById<SwitchCompat>(R.id.businessInsiderSwitch)
        businessInsiderSwitch.setOnClickListener { updateDatabasePublisher(businessInsiderSwitch.isChecked, businessInsiderSwitch.text.toString()) }
        publisherSwitches.add(businessInsiderSwitch)

        // Buzzfeed
        var buzzfeedSwitch = view.findViewById<SwitchCompat>(R.id.buzzfeedSwitch)
        buzzfeedSwitch.setOnClickListener { updateDatabasePublisher(buzzfeedSwitch.isChecked, buzzfeedSwitch.text.toString()) }
        publisherSwitches.add(buzzfeedSwitch)

        // CNN
        var cnnSwitch = view.findViewById<SwitchCompat>(R.id.cnnSwitch)
        cnnSwitch.setOnClickListener { updateDatabasePublisher(cnnSwitch.isChecked, cnnSwitch.text.toString()) }
        publisherSwitches.add(cnnSwitch)

        // ESPN
        var espnSwitch = view.findViewById<SwitchCompat>(R.id.espnSwitch)
        espnSwitch.setOnClickListener { updateDatabasePublisher(espnSwitch.isChecked, espnSwitch.text.toString()) }
        publisherSwitches.add(espnSwitch)

        // Financial Post
        var finantialPostSwitch = view.findViewById<SwitchCompat>(R.id.finantialPostSwitch)
        finantialPostSwitch.setOnClickListener { updateDatabasePublisher(finantialPostSwitch.isChecked, finantialPostSwitch.text.toString()) }
        publisherSwitches.add(finantialPostSwitch)

        // Fox News
        var foxNewsSwitch = view.findViewById<SwitchCompat>(R.id.foxNewsSwitch)
        foxNewsSwitch.setOnClickListener { updateDatabasePublisher(foxNewsSwitch.isChecked, foxNewsSwitch.text.toString()) }
        publisherSwitches.add(foxNewsSwitch)

        updateCategories()
        updatePublishers()
    }

    /**
     * Queries the FireStore database to get the user's selected categories and updates the UI.
     */
    fun updateCategories() {
        var uid: String? = tAuth.uid
        if (uid != null) {
            val user = db.collection("users").document(uid)
            user.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            updateUICategories(document.get("categories") as ArrayList<String>)
                        }
                    }
        }
    }

    //TODO - Reimplement, use a loop instead of multiple if statements
    /**
     * Updates the user interface with the category preferences for the currently signed in user.
     *
     * @param selectedCategories A list of selected categories for the currently signed in user.
     */
    fun updateUICategories(selectedCategories: ArrayList<String>) {
        for (switch in categorySwitches) {
            switch.isChecked = selectedCategories.contains(switch.text.toString())
        }
    }

    /**
     * Updates the FireStore database with the newly changed category preference.
     *
     * @param isChecked A boolean indicating whether the category is selected or not.
     * @param category The category preference which has been altered.
     */
    fun updateDatabaseCategory(isChecked: Boolean, category: String) {
        if (tAuth.uid != null) {
            var userQuery = UserQueryEngine(db)
            if (isChecked) {
                userQuery.addCategory(category, tAuth.uid!!)
            } else {
                userQuery.removeCategory(category, tAuth.uid!!)
            }
        }
    }

    /**
     * Queries the FireStore database to get the user's selected publishers and updates the UI.
     */
    fun updatePublishers() {
        var uid: String? = tAuth.uid
        if (uid != null) {
            val user = db.collection("users").document(uid)
            user.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            updateUIPublishers(document.get("publishers") as ArrayList<String>)
                        }
                    }
        }
    }

    /**
     * Updates the user interface with the publisher preferences for the currently signed in user.
     *
     * @param selectedPublishers A list of selected publishers for the currently signed in user.
     */
    fun updateUIPublishers(selectedPublishers: ArrayList<String>) {
        for (switch in publisherSwitches) {
            switch.isChecked = selectedPublishers.contains(switch.text.toString())
        }
    }

    /**
     * Updates the FireStore database with the newly changed publisher preference.
     *
     * @param isChecked A boolean indicating whether the publisher is selected or not.
     * @param publisher The publisher preference which has been altered.
     */
    fun updateDatabasePublisher(isChecked: Boolean, publisher: String) {
        if (tAuth.uid != null) {
            var userQuery = UserQueryEngine(db)
            if (isChecked) {
                userQuery.addPublisher(publisher, tAuth.uid!!)
            } else {
                userQuery.removePublisher(publisher, tAuth.uid!!)
            }
        }
    }
}