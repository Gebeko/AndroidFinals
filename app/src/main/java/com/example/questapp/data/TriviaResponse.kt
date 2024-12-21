package com.example.questapp.data

import android.text.Html

data class TriviaResponse(
    val response_code: Int,
    val results: List<Question>
)

data class Question(
    val type: String,
    val difficulty: String,
    val category: String,
    val question: String,
    val correct_answer: String,
    val incorrect_answers: List<String>
) {
    fun getDecodedQuestion(): String {
        return Html.fromHtml(question, Html.FROM_HTML_MODE_LEGACY).toString()
    }

    fun getDecodedCorrectAnswer(): String {
        return Html.fromHtml(correct_answer, Html.FROM_HTML_MODE_LEGACY).toString()
    }

    fun getDecodedIncorrectAnswers(): List<String> {
        return incorrect_answers.map {
            Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY).toString()
        }
    }
}
