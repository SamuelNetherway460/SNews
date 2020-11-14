package com.example.snews.fragments

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.snews.R

class DiscoverFragment : Fragment() {

    private val sharedPrefFile = "com.example.snews.discoverprefs"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.discover_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var discoverPreferences = activity!!.getSharedPreferences(sharedPrefFile, MODE_PRIVATE)
        val preferencesEditor: SharedPreferences.Editor = discoverPreferences!!.edit()
        //preferencesEditor.putString("test", "success")
        //preferencesEditor.commit()

        println(discoverPreferences.getString("test", "empty"))
        println(discoverPreferences.getString("test", "empty"))
        println(discoverPreferences.getString("test", "empty"))
        println(discoverPreferences.getString("test", "empty"))
    }
}