package com.example.snews.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.snews.R

//TODO - Full XML Check
//TODO - Documentation
/**
 *
 */
class GamesFragment : Fragment() {

    //TODO - Documentation
    /**
     *
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.games_fragment, container, false)
    }
}