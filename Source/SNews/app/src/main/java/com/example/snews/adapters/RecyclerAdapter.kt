package com.example.snews.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.snews.R
import com.example.snews.fragments.ArticleViewerFragment
import com.example.snews.models.Article
import com.example.snews.utilities.Constants
import com.squareup.picasso.Picasso

/**
 * Manages the article data displayed in the recycler view and navigation to the article viewer
 * fragment.
 *
 * @param articles An array list of articles to display in the recycler view.
 * @param activity The fragment activity which the recycler view is located in.
 * @author Samuel Netherway
 */
class RecyclerAdapter(private val articles: ArrayList<Article>, private val activity: FragmentActivity,
                      private val senderFragment: String)
    : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    /**
     * Parent class for handling layout inflation and child view use.
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var mediumArticleTitle: TextView
        var mediumArticlePublisher: TextView
        var mediumArticleDateTime: TextView
        var mediumArticleImage: ImageView

        init {
            mediumArticleTitle = itemView.findViewById(R.id.mediumRowArticleTitle)
            mediumArticlePublisher = itemView.findViewById(R.id.mediumRowCategory)
            mediumArticleDateTime = itemView.findViewById(R.id.mediumRowDatetime)
            mediumArticleImage = itemView.findViewById(R.id.mediumRowArticleImage)
        }
    }

    /**
     * Navigates the user to the article viewer fragment to display the article.
     *
     * @param article The article to display.
     */
    private fun navigateToArticleViewerFragment(article: Article) {
        val articleViewerFragment = ArticleViewerFragment(article, senderFragment)
        val fragmentManager = activity!!.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_main, articleViewerFragment,
                Constants.ARTICLE_VIEWER_FRAGMENT_TAG)
        fragmentTransaction.commit()
    }

    /**
     * Inflates the views using the row layout.
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.medium_recycler_row, viewGroup, false)
        return ViewHolder(v)
    }

    /**
     * Binds the data to the recycler row.
     */
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        var article: Article = articles.get(i)

        viewHolder.mediumArticleTitle.text = article.getTitle()
        viewHolder.mediumArticlePublisher.text = article.getSource()?.getName() ?: Constants.EMPTY_STRING
        viewHolder.mediumArticleDateTime.text = article.getPublishedAt()

        // Load article image
        // Displays local no article image if the article image cannot be loaded
        Picasso
            .get()
            .load(article.getUrlToImage())
            .placeholder(R.drawable.ic_no_article_image)
            .error(R.drawable.ic_no_article_image)
            .into(viewHolder.mediumArticleImage)

        viewHolder.itemView.setOnClickListener(View.OnClickListener {
            navigateToArticleViewerFragment(article)
        })
    }

    /**
     * @return The number of articles to be displayed in the recycler view.
     */
    override fun getItemCount(): Int {
        return articles.size
    }
}