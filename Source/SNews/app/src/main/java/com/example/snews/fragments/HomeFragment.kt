package com.example.snews.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.snews.R
import androidx.recyclerview.widget.RecyclerView
import com.example.snews.adapters.RecyclerAdapter
import com.example.snews.utilities.parsers.ArticleParser
import com.koushikdutta.ion.Ion
import org.json.JSONObject

class HomeFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchArticles(view)
    }

    fun fetchArticles(view: View) {
        Ion.with(this)
                .load("GET", "https://newsapi.org/v2/top-headlines?sources=bbc-news&apiKey=d3629af64f934b1889b1fc3afb716b3c")
                .setHeader("user-agent", "insomnia/2020.4.1")
                .asString()
                .setCallback { ex, result ->
                    startRecyclerView(view, result)
                }
    }

    // Change method contents to match module recycler view demo
    fun startRecyclerView(view: View, result: String) {
        var recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        // Start recylcer view population
        recyclerView.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            adapter = RecyclerAdapter(ArticleParser.parseArticleGroup(JSONObject(result)))
        }
    }
}