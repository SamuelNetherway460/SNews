package com.example.snews.models

/**
 * Game Question model for holding key data on a question used in the games fragment.
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

    init {
        this.correctArticle = correctArticle
        this.question = question
        this.answerOne = answerOne
        this.answerTwo = answerTwo
        this.answerThree = answerThree
        this.answerFour = answerFour
        this.correctAnswer = correctAnswer
    }

    fun getCorrectArticle() : Article? {
        return this.correctArticle
    }

    fun getQuestion() : String? {
        return this.question
    }

    fun getAnswerOne() : String? {
        return this.answerOne
    }

    fun getAnswerTwo() : String? {
        return this.answerTwo
    }

    fun getAnswerThree() : String? {
        return this.answerThree
    }

    fun getAnswerFour() : String? {
        return this.answerFour
    }

    fun getCorrectAnswer() : Int? {
        return this.correctAnswer
    }
}