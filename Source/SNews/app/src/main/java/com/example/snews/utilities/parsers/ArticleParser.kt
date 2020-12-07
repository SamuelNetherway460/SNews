package com.example.snews.utilities.parsers

import com.example.snews.models.Article
import com.example.snews.models.ArticleGroup
import com.example.snews.models.Source
import org.json.JSONArray
import org.json.JSONObject

//TODO - Convert strings to variables
/**
 * A class used for parsing raw JSON article data.
 *
 * @author Samuel Netherway
 */
class ArticleParser {

    companion object {

        /**
         * Parses a JSON article group object to a article group object.
         *
         * @param articleGroup JSON object containing the raw article group data.
         */
        fun parseArticleGroup(articleGroup: JSONObject): ArticleGroup {
            var status: String? = articleGroup.getString("status")
            var totalResults: Int? = articleGroup.getString("totalResults").toInt()
            var articles: ArrayList<Article> = parseArticles(articleGroup.getJSONArray("articles"))
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
            var source: Source? = parseSource(jsonArticle.getJSONObject("source"))
            var author: String? = jsonArticle.getString("author")
            var title: String? = jsonArticle.getString("title")
            var description: String? = jsonArticle.getString("description")
            var url: String? = jsonArticle.getString("url")
            var urlToImage: String? = jsonArticle.getString("urlToImage")
            var publishedAt: String? = jsonArticle.getString("publishedAt")
            var content: String? = jsonArticle.getString("content")
            return Article(jsonArticle, source, author, title, description, url, urlToImage, publishedAt, content)
        }

        /**
         * Converts a JSON source object to a source object.
         *
         * @param jsonSource A JSON object containing the raw source data on an article.
         * @return A single source object.
         */
        fun parseSource(jsonSource: JSONObject): Source {
            var id: String? = jsonSource.getString("id")
            var name: String? = jsonSource.getString("name")
            return Source(id, name)
        }
    }
}