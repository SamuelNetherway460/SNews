package com.example.snews.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.snews.R
import com.example.snews.models.MyModel

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    /*
    * A function to add article data to a list of MyModel
    */
    private fun populateList(): ArrayList<MyModel> {
        val list = ArrayList<MyModel>()

        return list
    }
}