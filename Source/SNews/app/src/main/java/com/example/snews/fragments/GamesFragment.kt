package com.example.snews.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.snews.R
import com.example.snews.models.Article
import com.example.snews.models.GameQuestion
import com.example.snews.utilities.Constants
import com.example.snews.utilities.parsers.ArticleParser
import com.squareup.picasso.Picasso
import org.json.JSONObject

/**
 * Fragment for controlling news themed multiple choice games.
 *
 * @author Samuel Netherway
 */
class GamesFragment : Fragment() {

    private var questionImageView: ImageView? = null
    private var buttonA: Button? = null
    private var buttonB: Button? = null
    private var buttonC: Button? = null
    private var buttonD: Button? = null
    private var viewArticleButton: Button? = null
    private var nextQuestionButton: Button? = null
    private var answerTextView: TextView? = null

    private var currentQuestion: GameQuestion? = null

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

    /**
     * Perform basic fragment setup.
     *
     * @param view The view hierarchy associated with the fragment.
     * @param savedInstanceState The saved state of the application.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        questionImageView = view.findViewById(R.id.questionImageView)
        buttonA = view.findViewById(R.id.buttonA)
        buttonB = view.findViewById(R.id.buttonB)
        buttonC = view.findViewById(R.id.buttonC)
        buttonD = view.findViewById(R.id.buttonD)
        viewArticleButton = view.findViewById(R.id.viewArticleButton)
        nextQuestionButton = view.findViewById(R.id.nextQuestionButton)
        answerTextView = view.findViewById(R.id.answerTextView)

        buttonA!!.setOnClickListener {
            if (checkChoice(buttonA!!)) {
                displayCorrectText()
            } else {
                buttonA!!.backgroundTintList = resources.getColorStateList(R.color.incorrectButton)
                displayIncorrectText()
            }
            displayCorrectAnswer()
            disableButtons()
        }

        buttonB!!.setOnClickListener {
            if (checkChoice(buttonB!!)) {
                displayCorrectText()
            } else {
                buttonB!!.backgroundTintList = resources.getColorStateList(R.color.incorrectButton)
                displayIncorrectText()
            }
            displayCorrectAnswer()
            disableButtons()
        }

        buttonC!!.setOnClickListener {
            if (checkChoice(buttonC!!)) {
                displayCorrectText()
            } else {
                buttonC!!.backgroundTintList = resources.getColorStateList(R.color.incorrectButton)
                displayIncorrectText()
            }
            displayCorrectAnswer()
            disableButtons()
        }

        buttonD!!.setOnClickListener {
            if (checkChoice(buttonD!!)) {
                displayCorrectText()
            } else {
                buttonD!!.backgroundTintList = resources.getColorStateList(R.color.incorrectButton)
                displayIncorrectText()
            }
            displayCorrectAnswer()
            disableButtons()
        }

        nextQuestionButton!!.setOnClickListener {
            newRound()
        }

        viewArticleButton!!.setOnClickListener {
            if (currentQuestion!!.getCorrectArticle() != null) {
                navigateToArticleViewerFragment()
            }
        }
    }

    /**
     * When the fragment is started, start a new round.
     */
    override fun onStart() {
        super.onStart()
        newRound()
    }

    /**
     * Displays correct on screen.
     */
    private fun displayCorrectText() {
        answerTextView!!.setText(resources.getString(R.string.correct))
    }

    /**
     * Displays incorrect on screen.
     */
    private fun displayIncorrectText() {
        answerTextView!!.setText(resources.getString(R.string.incorrect))
    }

    /**
     * Determines whether the user's chosen answer was correct.
     *
     * @param view The answer button pressed.
     * @return A boolean indicating whether the correct button was pressed.
     */
    private fun checkChoice(view: View) : Boolean {
        if (view == buttonA && currentQuestion!!.getCorrectAnswer() == 1) return true
        if (view == buttonB && currentQuestion!!.getCorrectAnswer() == 2) return true
        if (view == buttonC && currentQuestion!!.getCorrectAnswer() == 3) return true
        if (view == buttonD && currentQuestion!!.getCorrectAnswer() == 4) return true
        return false
    }

    /**
     * Displays the correct answer on screen by changing the relevant button to green.
     */
    private fun displayCorrectAnswer() {
        if (currentQuestion!!.getCorrectAnswer() == 1) {
            buttonA!!.backgroundTintList = resources.getColorStateList(R.color.correctButton)
        }
        if (currentQuestion!!.getCorrectAnswer() == 2) {
            buttonB!!.backgroundTintList = resources.getColorStateList(R.color.correctButton)
        }
        if (currentQuestion!!.getCorrectAnswer() == 3) {
            buttonC!!.backgroundTintList = resources.getColorStateList(R.color.correctButton)
        }
        if (currentQuestion!!.getCorrectAnswer() == 4) {
            buttonD!!.backgroundTintList = resources.getColorStateList(R.color.correctButton)
        }
    }

    /**
     * Sets up a new round.
     */
    private fun newRound() {
        resetUI()
        currentQuestion = generateRandomGameQuestion()
        populateScreen()
    }

    /**
     * Resets the user interface ready for the next round / question.
     */
    private fun resetUI() {
        buttonA!!.backgroundTintList = resources.getColorStateList(R.color.backgroundGreyMedium)
        buttonB!!.backgroundTintList = resources.getColorStateList(R.color.backgroundGreyMedium)
        buttonC!!.backgroundTintList = resources.getColorStateList(R.color.backgroundGreyMedium)
        buttonD!!.backgroundTintList = resources.getColorStateList(R.color.backgroundGreyMedium)
        enableButtons()
        answerTextView!!.text = Constants.EMPTY_STRING
    }

    /**
     * Enables all answer buttons so that they can be pressed.
     */
    private fun enableButtons() {
        buttonA!!.isClickable = true
        buttonB!!.isClickable = true
        buttonC!!.isClickable = true
        buttonD!!.isClickable = true
    }

    /**
     * Disables all answer buttons so that they cannot be pressed.
     */
    private fun disableButtons() {
        buttonA!!.isClickable = false
        buttonB!!.isClickable = false
        buttonC!!.isClickable = false
        buttonD!!.isClickable = false
    }

    /**
     * Populates various on screen views with the question based data.
     */
    private fun populateScreen() {
        Picasso
                .get()
                .load(currentQuestion!!.getCorrectArticle()!!.getUrlToImage())
                .placeholder(R.drawable.ic_no_article_image)
                .error(R.drawable.ic_no_article_image)
                .into(questionImageView)
        buttonA!!.text = currentQuestion!!.getAnswerOne()
        buttonB!!.text = currentQuestion!!.getAnswerTwo()
        buttonC!!.text = currentQuestion!!.getAnswerThree()
        buttonD!!.text = currentQuestion!!.getAnswerFour()
    }

    /**ARTICLE_VIEWER_FRAGMENT_TAG
     * Generates a random game question.
     *
     * @return A random game question.
     */
    private fun generateRandomGameQuestion() : GameQuestion {
        var questionArticles = getFourRandomArticles()
        var answerOne = questionArticles.get(0).getTitle()!!
        var answerTwo = questionArticles.get(1).getTitle()!!
        var answerThree = questionArticles.get(2).getTitle()!!
        var answerFour = questionArticles.get(3).getTitle()!!
        var correctAnswer = (1..4).random()
        var correctArticle = questionArticles.get(correctAnswer - 1)

        return GameQuestion(correctArticle, resources.getString(R.string.guess_the_title),
                answerOne, answerTwo, answerThree, answerFour, correctAnswer)
    }

    /**
     * Gets four random articles from the internal article store.
     *
     * @return A array list of four articles.
     */
    private fun getFourRandomArticles() : ArrayList<Article> {
        var articles = getArticles()
        var fourArticles = ArrayList<Article>()

        var randomArticleOne = articles.get((0..(articles.size - 1)).random())
        var randomArticleTwo = articles.get((0..(articles.size - 1)).random())
        var randomArticleThree = articles.get((0..(articles.size - 1)).random())
        var randomArticleFour = articles.get((0..(articles.size - 1)).random())

        fourArticles.add(randomArticleOne)
        fourArticles.add(randomArticleTwo)
        fourArticles.add(randomArticleThree)
        fourArticles.add(randomArticleFour)

        return fourArticles
    }

    /**
     * Read and parse the article data from internal storage.
     *
     * @return A list of articles.
     */
    private fun getArticles(): ArrayList<Article> {
        var articles = ArrayList<Article>()
        if (fileExist(Constants.ARTICLE_STORE_FILENAME)) {
            var jsonArticles = JSONObject(readArticleStorage())
                    .getJSONArray(Constants.ARTICLE_STORE_JSON_ARRAY_NAME)
            for (i in 0..jsonArticles.length() - 1) {
                articles.add(ArticleParser.parseArticle(jsonArticles.getJSONObject(i)))
            }
        }
        return articles
    }

    /**
     * Checks whether a file exists in internal storage.
     *
     * @param filename The name of the file which is being check for.
     * @return A boolean indicating whether the file exists in internal storage or not.
     */
    private fun fileExist(filename: String): Boolean {
        val file = context!!.getFileStreamPath(filename)
        return file.exists()
    }

    /**
     * Reads the article data from internal storage.
     *
     * @return The raw article data.
     */
    private fun readArticleStorage() : String {
        activity!!.openFileInput(Constants.ARTICLE_STORE_FILENAME).use {
            return it.readBytes().decodeToString()
        }
    }

    /**
     * Navigates the user to the article viewer fragment to display the article.
     */
    private fun navigateToArticleViewerFragment() {
        val articleViewerFragment = ArticleViewerFragment(currentQuestion!!.getCorrectArticle()!!,
                Constants.GAMES_FRAGMENT_TAG)
        val fragmentManager = activity!!.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_main, articleViewerFragment,
                Constants.ARTICLE_VIEWER_FRAGMENT_TAG)
        fragmentTransaction.commit()
    }
}