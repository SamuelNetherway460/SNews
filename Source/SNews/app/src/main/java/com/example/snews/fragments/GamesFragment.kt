package com.example.snews.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.snews.R

//TODO - Full XML Check
//TODO - Documentation
/**
 * Fragment for controlling news themed multiple choice games.
 */
class GamesFragment : Fragment() {

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
        return inflater.inflate(R.layout.games_fragment, container, false)
    }

    //TODO - Implement or remove
    /**
     *
     */
    override fun onPause() {
        super.onPause()
        Log.d(ContentValues.TAG, "GAMES FRAGMENT - ON PAUSE CALLED")
    }

    //TODO - Implement or remove
    /**
     *
     */
    override fun onResume() {
        super.onResume()
        Log.d(ContentValues.TAG, "GAMES FRAGMENT - ON RESUME CALLED")
    }

    //TODO - Implement or remove
    /**
     *
     */
    override fun onStop() {
        super.onStop()
        Log.d(ContentValues.TAG, "GAMES FRAGMENT - ON STOP CALLED")
    }

    //TODO - Implement or remove
    /**
     *
     */
    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(ContentValues.TAG, "GAMES FRAGMENT - ON DESTROY CALLED")
    }
}