package com.example.snews.models

/**
 * Game Question model for holding key data on a question used in the games fragment.
 *
 * @property correctArticle The article object of the correct article.
 * @property question The game question.
 * @property answerOne The first possible answer.
 * @property answerTwo The second possible answer.
 * @property answerThree The third possible answer.
 * @property answerFour The fourth possible answer.
 * @property correctAnswer An integer representing the correct answer.
 * @author Samuel Netherway
 */
class GameQuestion(correctArticle: Article, question: String, answerOne: String,
                   answerTwo: String, answerThree: String, answerFour: String, correctAnswer: Int) {

    private var correctArticle: Article? = null
    private var question: String? = null
    private var answerOne: String? = null
    private var answerTwo: String? = null
    private var answerThree: String? = null
    private var answerFour: String? = null
    private var correctAnswer: Int? = null

    /**
     * Sets the values for the new game question object.
     */
    init {
        this.correctArticle = correctArticle
        this.question = question
        this.answerOne = answerOne
        this.answerTwo = answerTwo
        this.answerThree = answerThree
        this.answerFour = answerFour
        this.correctAnswer = correctAnswer
    }

    /**
     * @return An article object of the correct article.
     */
    fun getCorrectArticle() : Article? {
        return this.correctArticle
    }

    /**
     * @return The game question.
     */
    fun getQuestion() : String? {
        return this.question
    }

    /**
     * @return The first possible answer.
     */
    fun getAnswerOne() : String? {
        return this.answerOne
    }

    /**
     * @return The second possible answer.
     */
    fun getAnswerTwo() : String? {
        return this.answerTwo
    }

    /**
     * @return The third possible answer.
     */
    fun getAnswerThree() : String? {
        return this.answerThree
    }

    /**
     * @return The forth possible answer.
     */
    fun getAnswerFour() : String? {
        return this.answerFour
    }

    /**
     * @return An integer representing the correct answer.
     */
    fun getCorrectAnswer() : Int? {
        return this.correctAnswer
    }
}