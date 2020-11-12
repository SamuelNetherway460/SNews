package com.example.snews.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.snews.R

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private val titles = arrayOf("Title One", "Title Two", "Title Three", "Title Four",
        "Title Five", "Title Six", "Title Seven", "Title Eight", "Title Nine", "Title Ten")

    private val categories = arrayOf("Category One", "Category Two", "Category Three",
        "Category Four", "Category Five", "Category Six", "Category Seven", "Category Eight",
        "Category Nine", "Category Ten")

    private val datetimes = arrayOf("01:00 1 August 2020", "02:00 2 August 2020",
        "03:00 3 August 2020", "04:00 4 August 2020", "05:00 5 August 2020", "06:00 6 August 2020",
        "07:00 7 August 2020", "08:00 8 August 2020", "09:00 9 August 2020", "10:00 10 August 2020")

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var articleTitle: TextView
        var articleCategory: TextView
        var articleDateTime: TextView

        init {
            articleTitle = itemView.findViewById(R.id.mediumRowArticleTitle)
            articleCategory = itemView.findViewById(R.id.mediumRowCategory)
            articleDateTime = itemView.findViewById(R.id.mediumRowDatetime)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.medium_recycler_row, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.articleTitle.text = titles[i]
        viewHolder.articleCategory.text = categories[i]
        viewHolder.articleDateTime.text = datetimes[i]

    }

    override fun getItemCount(): Int {
        return titles.size
    }
}