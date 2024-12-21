package com.example.questapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.questapp.activity.CategorySelectionActivity
import com.example.questapp.activity.ScoreActivity
import com.example.questapp.adapter.QuestionAdapter
import com.example.questapp.api.RetrofitInstance
import com.example.questapp.data.Category
import com.example.questapp.data.CategoryResponse
import com.example.questapp.data.Question
import com.example.questapp.data.TriviaResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var questionsRecyclerView: RecyclerView
    private lateinit var finishButton: Button
    private lateinit var questionAdapter: QuestionAdapter
    private var score = 0
    private var answeredCount = 0
    private var questions: List<Question> = emptyList()
    private var categories: List<Category> = emptyList()
    private var selectedCategoryId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        selectedCategoryId = intent.getIntExtra("categoryId", 0)
        if (selectedCategoryId == 0) {
            val intent = Intent(this, CategorySelectionActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            fetchQuestions()
        }

        setupRecyclerView()
        setupFinishButton()
    }

    private fun setupRecyclerView() {
        questionsRecyclerView = findViewById(R.id.questions_recycler_view)
        questionAdapter = QuestionAdapter { position, selectedAnswer ->
            handleAnswer(position, selectedAnswer)
        }

        questionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = questionAdapter
        }
    }

    private fun setupFinishButton() {
        finishButton = findViewById(R.id.finish_button)
        finishButton.setOnClickListener {
            if (answeredCount < questions.size) {
                Toast.makeText(
                    this,
                    "Please answer all questions before finishing",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                showFinalScore()
            }
        }
    }

    private fun handleAnswer(position: Int, selectedAnswer: String) {
        val question = questions[position]
        if (selectedAnswer == question.correct_answer) {
            score++
        }
        answeredCount++
    }

    private fun fetchCategories() {
        val api = RetrofitInstance.api
        api.getCategories().enqueue(object : Callback<CategoryResponse> {
            override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                if (response.isSuccessful) {
                    categories = response.body()?.trivia_categories ?: emptyList()
                    showCategorySelectionDialog()
                } else {
                    Toast.makeText(this@MainActivity, "Failed to load categories", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed to load categories", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showCategorySelectionDialog() {
        if (selectedCategoryId != null) return // Prevent showing the dialog again

        val categoryNames = categories.map { it.name }
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select a Category")
        builder.setItems(categoryNames.toTypedArray()) { _, which ->
            selectedCategoryId = categories[which].id
            fetchQuestions()
        }
        builder.setCancelable(false)
        builder.show()
    }
    private fun fetchQuestions() {
        val api = RetrofitInstance.api
        val categoryId = selectedCategoryId ?: 0

        api.getQuestions(categoryId).enqueue(object : Callback<TriviaResponse> {
            override fun onResponse(call: Call<TriviaResponse>, response: Response<TriviaResponse>) {
                if (response.isSuccessful) {
                    questions = response.body()?.results ?: emptyList()
                    questionAdapter.submitList(questions)
                } else {
                    Toast.makeText(this@MainActivity, "Failed to fetch questions", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TriviaResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed to fetch questions", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showFinalScore() {
        val intent = Intent(this, ScoreActivity::class.java)
        intent.putExtra("score", score)
        intent.putExtra("totalQuestions", questions.size)
        startActivity(intent)
        finish()
    }

    private fun resetQuiz() {
        score = 0
        answeredCount = 0
        selectedCategoryId = null
        fetchCategories()
    }
}