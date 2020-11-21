package com.example.snews.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.example.snews.R
import com.example.snews.utilities.database.queryEngines.UserQueryEngine
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

//TODO - Full XML Check
//TODO - Check over xml, put correct colors, i.e. iconColor
//TODO Add sign in check
//TODO Check null safety
/**
 * A fragment which provides functionality for the Discover screen of the app. The discover screen
 * allows the user to customise their experience by selecting certain news related criteria.
 *
 * @param tAuth The Firebase authentication instance used to sync user discover preferences with
 *              the FireStore database.
 * @author Samuel Netherway
 */
class DiscoverFragment(private val tAuth: FirebaseAuth, private val db: FirebaseFirestore) : Fragment() {

    private val BUSINESS: String = "business"
    private val ENTERTAINMENT: String = "entertainment"
    private val GENERAL: String = "general"
    private val HEALTH: String = "health"
    private val SCIENCE: String = "science"
    private val SPORTS: String = "sports"
    private val TECHNOLOGY: String = "technology"

    //TODO - Documentation
    /**
     *
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.discover_fragment, container, false)
    }

    //TODO - Documentation
    /**
     *
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var businessSwitch = view.findViewById<SwitchCompat>(R.id.businessSwitch)
        businessSwitch.setOnClickListener { updateDatabaseCategory(businessSwitch.isChecked, BUSINESS) }
        var entertainmentSwitch = view.findViewById<SwitchCompat>(R.id.entertainmentSwitch)
        entertainmentSwitch.setOnClickListener { updateDatabaseCategory(entertainmentSwitch.isChecked, ENTERTAINMENT) }
        var generalSwitch = view.findViewById<SwitchCompat>(R.id.generalSwitch)
        generalSwitch.setOnClickListener { updateDatabaseCategory(generalSwitch.isChecked, GENERAL) }
        var healthSwitch = view.findViewById<SwitchCompat>(R.id.healthSwitch)
        healthSwitch.setOnClickListener { updateDatabaseCategory(healthSwitch.isChecked, HEALTH) }
        var scienceSwitch = view.findViewById<SwitchCompat>(R.id.scienceSwitch)
        scienceSwitch.setOnClickListener { updateDatabaseCategory(scienceSwitch.isChecked, SCIENCE) }
        var sportsSwitch = view.findViewById<SwitchCompat>(R.id.sportsSwitch)
        sportsSwitch.setOnClickListener { updateDatabaseCategory(sportsSwitch.isChecked, SPORTS) }
        var technologySwitch = view.findViewById<SwitchCompat>(R.id.technologySwitch)
        technologySwitch.setOnClickListener { updateDatabaseCategory(technologySwitch.isChecked, TECHNOLOGY) }

        updateCategories()
    }

    /**
     * Queries the FireStore database to get the user's selected categories and updates the user
     * interface.
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

    /**
     * Queries the FireStore database to get the user's selected publishers and updates the user
     * interface.
     */
    fun updatePublishers() {
        var uid: String? = tAuth.uid
        if (uid != null) {
            val user = db.collection("users").document(uid)
            user.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            updateUICategories(document.get("publishers") as ArrayList<String>)
                        }
                    }
        }
    }

    //TODO - Reimplement, use a loop instead of multiple if statements
    /**
     * Updates the user interface with the category preferences for the currently signed in user.
     *
     * @param categories A list of selected categories for the currently signed in user.
     */
    fun updateUICategories(categories: ArrayList<String>) {
        var businessSwitch = view?.findViewById<SwitchCompat>(R.id.businessSwitch)
        var entertainmentSwitch = view?.findViewById<SwitchCompat>(R.id.entertainmentSwitch)
        var generalSwitch = view?.findViewById<SwitchCompat>(R.id.generalSwitch)
        var healthSwitch = view?.findViewById<SwitchCompat>(R.id.healthSwitch)
        var scienceSwitch = view?.findViewById<SwitchCompat>(R.id.scienceSwitch)
        var sportsSwitch = view?.findViewById<SwitchCompat>(R.id.sportsSwitch)
        var technologySwitch = view?.findViewById<SwitchCompat>(R.id.technologySwitch)

        if (categories.contains(BUSINESS)) {
            businessSwitch?.setChecked(true)
        } else {
            businessSwitch?.setChecked(false)
        }
        if (categories.contains(ENTERTAINMENT)) {
            entertainmentSwitch?.setChecked(true)
        } else {
            entertainmentSwitch?.setChecked(false)
        }
        if (categories.contains(GENERAL)) {
            generalSwitch?.setChecked(true)
        } else {
            generalSwitch?.setChecked(false)
        }
        if (categories.contains(HEALTH)) {
            healthSwitch?.setChecked(true)
        } else {
            healthSwitch?.setChecked(false)
        }
        if (categories.contains(SCIENCE)) {
            scienceSwitch?.setChecked(true)
        } else {
            scienceSwitch?.setChecked(false)
        }
        if (categories.contains(SPORTS)) {
            sportsSwitch?.setChecked(true)
        } else {
            sportsSwitch?.setChecked(false)
        }
        if (categories.contains(TECHNOLOGY)) {
            technologySwitch?.setChecked(true)
        } else {
            technologySwitch?.setChecked(false)
        }
    }

    //TODO - Implement function
    /**
     * Updates the user interface with the publisher preferences for the currently signed in user.
     *
     * @param publishers A list of selected publishers for the currently signed in user.
     */
    fun updateUIPublishers(publishers: ArrayList<String>) {

    }

    /**
     * Updates the FireStore database with the newly changed category preference.
     *
     * @param isChecked A boolean indicating whether the category is selected or not.
     * @param category The category preference which has been altered.
     */
    fun updateDatabaseCategory(isChecked: Boolean, category: String) {
        if (tAuth.uid != null) {
            var userQuery = UserQueryEngine(db, tAuth.uid!!)
            if (isChecked) {
                userQuery.addCategory(category)
            } else {
                userQuery.removeCategory(category)
            }
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
            var userQuery = UserQueryEngine(db, tAuth.uid!!)
            if (isChecked) {
                userQuery.addPublisher(publisher)
            } else {
                userQuery.removePublisher(publisher)
            }
        }
    }
}