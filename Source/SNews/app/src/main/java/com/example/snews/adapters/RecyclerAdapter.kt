package com.example.snews.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.snews.R
import com.example.snews.fragments.ArticleViewerFragment
import com.example.snews.models.Article
import com.example.snews.utilities.Constants

//TODO - Multiple types of rows
//TODO - Documentation, look at demo video for correct method layouts and functions
//TODO - Documentation
/**
 * Manages the article data displayed in the recycler view and navigation to the article viewer
 * fragment.
 *
 * @param articles An array list of articles to display in the recycler view.
 * @param activity
 * @author Samuel Netherway
 */
class RecyclerAdapter(private val articles: ArrayList<Article>, private val activity: FragmentActivity)
    : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    //TODO - Documentation
    /**
     *
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var mediumArticleTitle: TextView
        var mediumArticleCategory: TextView
        var mediumArticleDateTime: TextView

        /*
        var largeArticleTitle: TextView
        var largeArticleDescription: TextView
        var largeArticleCategory: TextView
        var largeArticleDateTime: TextView
        var largeArticleImage: ImageView
         */

        //TODO - Documentation
        /**
         * @constructor
         */
        init {
            mediumArticleTitle = itemView.findViewById(R.id.mediumRowArticleTitle)
            mediumArticleCategory = itemView.findViewById(R.id.mediumRowCategory)
            mediumArticleDateTime = itemView.findViewById(R.id.mediumRowDatetime)

            /*
            largeArticleImage = itemView.findViewById(R.id.largeRowArticleImage)
            largeArticleTitle = itemView.findViewById(R.id.largeRowArticleTitle)
            largeArticleDescription = itemView.findViewById(R.id.largeRowArticleDescription)
            largeArticleCategory = itemView.findViewById(R.id.largeRowCategory)
            largeArticleDateTime = itemView.findViewById(R.id.largeRowDatetime)
             */
        }
    }

    /**
     * Navigates the user to the article viewer fragment to display the article.
     *
     * @param article The article to display.
     */
    private fun navigateToArticleViewerFragment(article: Article) {
        val articleViewerFragment = ArticleViewerFragment(article)
        val fragmentManager = activity!!.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_main, articleViewerFragment, "ArticleViewerFragment") //TODO - Check what the value of the tag paramaeter is meant to be
        fragmentTransaction.commit()
    }

    //TODO - Documentation
    /**
     *
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.medium_recycler_row, viewGroup, false)
        return ViewHolder(v)

        /*
        val v = LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.large_recycler_row, viewGroup, false)
            return ViewHolder(v)
         */
    }

    //TODO - Documentation
    /**
     *
     */
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        var article: Article = articles.get(i)

        //TODO - Use picasso to display image
        viewHolder.mediumArticleTitle.text = article.getTitle()
        viewHolder.mediumArticleCategory.text = article.getSource()?.getName() ?: Constants.EMPTY_STRING // TODO Replace with category
        viewHolder.mediumArticleDateTime.text = article.getPublishedAt()

        /*
        viewHolder.largeArticleTitle.text = article.getTitle()
        viewHolder.largeArticleDescription.text = article.getDescription()
        viewHolder.largeArticleCategory.text = "category"
        viewHolder.largeArticleDateTime.text = article.getPublishedAt()
         */

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