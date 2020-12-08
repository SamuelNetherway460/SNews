package com.example.snews.utilities.parsers

import com.example.snews.models.Article
import com.example.snews.models.ArticleGroup
import com.example.snews.models.Source
import org.json.JSONArray
import org.json.JSONObject

/**
 * A class used for parsing raw JSON article data.
 *
 * @author Samuel Netherway
 */
class ArticleParser {

    companion object {

        // Article group JSON fields
        private const val STATUS = "status"
        private const val TOTAL_RESULTS = "totalResults"
        private const val ARTICLES = "articles"

        // Article JSON fields
        private const val SOURCE = "source"
        private const val AUTHOR = "author"
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
        private const val URL = "url"
        private const val URL_TO_IMAGE = "urlToImage"
        private const val PUBLISHED_AT = "publishedAt"
        private const val CONTENT = "content"

        // Source JSON fields
        private const val ID = "id"
        private const val NAME = "name"

        /**
         * Parses a JSON article group object to a article group object.
         *
         * @param articleGroup JSON object containing the raw article group data.
         */
        fun parseArticleGroup(articleGroup: JSONObject): ArticleGroup {
            var status: String? = articleGroup.getString(STATUS)
            var totalResults: Int? = articleGroup.getString(TOTAL_RESULTS).toInt()
            var articles: ArrayList<Article> = parseArticles(articleGroup.getJSONArray(ARTICLES))
            return ArticleGroup(status, totalResults, articles)
        }

        /**
         * Converts a list of JSON article objects to a array list of article objects.
         *
         * @param jsonArticles Array of JSON objects containing the raw article data.
         * @param totalResults The total number of articles contained in the JSON array.
         * @return An array list of article objects.
         */
        fun parseArticles(jsonArticles: JSONArray): ArrayList<Article> {
            var articles: ArrayList<Article> = ArrayList()
            // Iterate through the JSON array of articles and add articles to the articles array
            for (i in 0..jsonArticles.length()-1) {
                articles.add(parseArticle(jsonArticles.getJSONObject(i)))
            }
            return articles
        }

        /**
         * Converts a JSON article object to a article object.
         *
         * @param jsonArticle A single JSON object containing the raw data for one article.
         * @return A single article object.
         */
        fun parseArticle(jsonArticle: JSONObject): Article {
            var source: Source? = parseSource(jsonArticle.getJSONObject(SOURCE))
            var author: String? = jsonArticle.getString(AUTHOR)
            var title: String? = jsonArticle.getString(TITLE)
            var description: String? = jsonArticle.getString(DESCRIPTION)
            var url: String? = jsonArticle.getString(URL)
            var urlToImage: String? = jsonArticle.getString(URL_TO_IMAGE)
            var publishedAt: String? = jsonArticle.getString(PUBLISHED_AT)
            var content: String? = jsonArticle.getString(CONTENT)
            return Article(jsonArticle, source, author, title, description, url, urlToImage, publishedAt, content)
        }

        /**
         * Converts a JSON source object to a source object.
         *
         * @param jsonSource A JSON object containing the raw source data on an article.
         * @return A single source object.
         */
        fun parseSource(jsonSource: JSONObject): Source {
            var id: String? = jsonSource.getString(ID)
            var name: String? = jsonSource.getString(NAME)
            return Source(id, name)
        }
    }
}