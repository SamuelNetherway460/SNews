package com.example.snews.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.snews.R
import com.example.snews.models.Article
import com.example.snews.models.ArticleGroup
import java.net.URL


class RecyclerAdapter(private val articleGroup: ArticleGroup) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /*
        var mediumArticleTitle: TextView
        var mediumArticleCategory: TextView
        var mediumArticleDateTime: TextView
         */

        var largeArticleTitle: TextView
        var largeArticleDescription: TextView
        var largeArticleCategory: TextView
        var largeArticleDateTime: TextView
        var largeArticleImage: ImageView

        init {

            /*
            mediumArticleTitle = itemView.findViewById(R.id.mediumRowArticleTitle)
            mediumArticleCategory = itemView.findViewById(R.id.mediumRowCategory)
            mediumArticleDateTime = itemView.findViewById(R.id.mediumRowDatetime)
            */

            largeArticleImage = itemView.findViewById(R.id.largeRowArticleImage)
            largeArticleTitle = itemView.findViewById(R.id.largeRowArticleTitle)
            largeArticleDescription = itemView.findViewById(R.id.largeRowArticleDescription)
            largeArticleCategory = itemView.findViewById(R.id.largeRowCategory)
            largeArticleDateTime = itemView.findViewById(R.id.largeRowDatetime)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        /*
        val v = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.medium_recycler_row, viewGroup, false)
        return ViewHolder(v)
        */

        val v = LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.large_recycler_row, viewGroup, false)
            return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        var article: Article = articleGroup.getArticles()!!.get(i)

        /*
        viewHolder.mediumArticleTitle.text = article.getTitle()
        viewHolder.mediumArticleCategory.text = "category" // TODO Replace with category
        viewHolder.mediumArticleDateTime.text = article.getPublishedAt()
         */

        viewHolder.largeArticleTitle.text = article.getTitle()
        viewHolder.largeArticleDescription.text = article.getDescription()
        viewHolder.largeArticleCategory.text = "category"
        viewHolder.largeArticleDateTime.text = article.getPublishedAt()
    }

    override fun getItemCount(): Int {
        return articleGroup.getArticles()!!.size
    }
}