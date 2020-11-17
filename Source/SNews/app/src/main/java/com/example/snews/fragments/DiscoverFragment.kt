package com.example.snews.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.example.snews.R
import com.example.snews.database.queries.UserQuery
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

//TODO Add sign in check
//TODO Check null safety
class DiscoverFragment(private val tAuth: FirebaseAuth) : Fragment() {

    // TODO Remove
    //private val sharedPrefFile = "com.example.snews.discoverprefs"

    private val BUSINESS: String = "business"
    private val ENTERTAINMENT: String = "entertainment"
    private val GENERAL: String = "general"
    private val HEALTH: String = "health"
    private val SCIENCE: String = "science"
    private val SPORTS: String = "sports"
    private val TECHNOLOGY: String = "technology"

    // TODO Possibly put in main activity
    var db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.discover_fragment, container, false)
    }

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

        // TODO Remove
        /*
        var discoverPreferences = activity!!.getSharedPreferences(sharedPrefFile, MODE_PRIVATE)
        val preferencesEditor: SharedPreferences.Editor = discoverPreferences!!.edit()
        //preferencesEditor.putString("test", "success")
        //preferencesEditor.commit()

        println(discoverPreferences.getString("test", "empty"))
        println(discoverPreferences.getString("test", "empty"))
        println(discoverPreferences.getString("test", "empty"))
        println(discoverPreferences.getString("test", "empty"))
        */
    }

    // TODO Remove
    fun getFirstName(view: View) {
        var test = view.findViewById<TextView>(R.id.businessText)

        val user = db.collection("users").document("irmdrh4xNoX53JuOH24SUO5uJHt2")
        user.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        test.setText(document.getString("first_name"))
                    }
                }
                .addOnFailureListener {
                    test.setText("Failure")
                }
    }

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

    fun updateDatabaseCategory(isChecked: Boolean, category: String) {
        var userQuery = UserQuery(db)
        var uid: String? = tAuth.uid
        if (uid != null) {
            if (isChecked) {
                userQuery.addCategory(uid, category)
            } else {
                userQuery.removeCategory(uid, category)
            }
        }
    }

    fun getSelectedCategories() : ArrayList<String>? {

        var businessSwitch = view?.findViewById<SwitchCompat>(R.id.businessSwitch)
        var entertainmentSwitch = view?.findViewById<SwitchCompat>(R.id.entertainmentSwitch)
        var generalSwitch = view?.findViewById<SwitchCompat>(R.id.generalSwitch)
        var healthSwitch = view?.findViewById<SwitchCompat>(R.id.healthSwitch)
        var scienceSwitch = view?.findViewById<SwitchCompat>(R.id.scienceSwitch)
        var sportsSwitch = view?.findViewById<SwitchCompat>(R.id.sportsSwitch)
        var technologySwitch = view?.findViewById<SwitchCompat>(R.id.technologySwitch)

        var selectedCategories: ArrayList<String>? = null

        if (businessSwitch!!.isChecked) selectedCategories?.add("Business")
        if (entertainmentSwitch!!.isChecked) selectedCategories?.add("Entertainment")
        if (generalSwitch!!.isChecked) selectedCategories?.add("General")
        if (healthSwitch!!.isChecked) selectedCategories?.add("Health")
        if (scienceSwitch!!.isChecked) selectedCategories?.add("Science")
        if (sportsSwitch!!.isChecked) selectedCategories?.add("Sports")
        if (technologySwitch!!.isChecked) selectedCategories?.add("Technology")

        return selectedCategories
    }

    fun addData() {
        val userOne: MutableMap<String, Any> = HashMap()
        userOne["first_name"] = "Martin"
        userOne["last_name"] = "Netherway"
        userOne["favourite_topics"] = arrayListOf("Health", "Science", "Politics")

        // Add a new document with a generated ID
        db.collection("users").add(userOne)
    }
}