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

    // Change method contents to match module recycler view demo
    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        var recyclerView = itemView.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            adapter = RecyclerAdapter()
        }
    }

    // Get api data here and pass into recycler adapter



































    /*
    fun getArticle() {
        Ion.with(this)
            .load("GET", "https://newsapi.org/v2/top-headlines?sources=bbc-news&pageSize=1&apiKey=d3629af64f934b1889b1fc3afb716b3c")
            .setHeader("user-agent", "insomnia/2020.4.1")
            .asString()
            .setCallback { ex, result ->
                consolePrintArticle(result)
            }
    }

    fun consolePrintArticle(data: String) {
        val myJSON = JSONObject(data)
        val articleTitle = myJSON.getString("status")

        println("*****************************HERE*****************************")
        println("*****************************HERE*****************************")
        println("*****************************HERE*****************************")
        println("*****************************HERE*****************************")
        println("Status: " + articleTitle)
    }
    */
}