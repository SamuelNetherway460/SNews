package com.example.snews.utilities.parsers

import com.example.snews.models.Article
import com.example.snews.models.ArticleGroup
import com.example.snews.models.Source
import org.json.JSONArray
import org.json.JSONObject

class ArticleParser {

    companion object {
        /**
         * Parses a json article group object to a article group object
         */
        fun parseArticleGroup(articleGroup: JSONObject): ArticleGroup {
            var status: String? = articleGroup.getString("status")
            var totalResults: Int = articleGroup.getString("totalResults").toInt()
            var articles: ArrayList<Article> = parseArticles(articleGroup.getJSONArray("articles"), totalResults) // TODO Implement null safety if no articles are returned
            return ArticleGroup(status, totalResults, articles)
        }

        /**
         * Converts a list of json article objects to a array list of article objects
         */
        fun parseArticles(jsonArticles: JSONArray, totalResults: Int): ArrayList<Article> {
            var articles: ArrayList<Article> = ArrayList()
            // Iterate through the JSON array of articles and add articles to the articles array
            for (i in 0..jsonArticles.length()-1) {
                articles.add(parseArticle(jsonArticles.getJSONObject(i)))
            }
            return articles
        }

        /**
         * Converts a json article object to a article object
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
            return Article(source, author, title, description, url, urlToImage, publishedAt, content)
        }

        /**
         * Converts a json source object to a source object
         */
        fun parseSource(jsonSource: JSONObject): Source {
            var id: String? = jsonSource.getString("id")
            var name: String = jsonSource.getString("name")
            return Source(id, name)
        }
    }
}