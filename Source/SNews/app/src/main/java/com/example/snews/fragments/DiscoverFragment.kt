package com.example.snews.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.snews.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class DiscoverFragment(private val tAuth: FirebaseAuth) : Fragment() {

    //private val sharedPrefFile = "com.example.snews.discoverprefs"

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

        //addData()

        var test = view.findViewById<TextView>(R.id.businessText)
        test.setText(tAuth.uid)

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

    fun addData() {
        val userOne: MutableMap<String, Any> = HashMap()
        userOne["first_name"] = "Martin"
        userOne["last_name"] = "Netherway"
        userOne["favourite_topics"] = arrayListOf("Health", "Science", "Politics")

        // Add a new document with a generated ID
        db.collection("users").add(userOne)
    }
}