package com.example.questapp.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.questapp.R
import com.example.questapp.adapter.QuestionAdapter
import com.example.questapp.api.RetrofitInstance
import com.example.questapp.data.Question
import com.example.questapp.data.TriviaResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuizFragment : Fragment() {
    private lateinit var questionsRecyclerView: RecyclerView
    private lateinit var finishButton: Button
    private lateinit var questionAdapter: QuestionAdapter
    private val categoryId: Int by lazy {
        arguments?.getInt("categoryId", 0) ?: 0
    }
    private val categoryName: String by lazy {
        arguments?.getString("categoryName", "") ?: ""
    }
    private var score = 0
    private var answeredCount = 0
    private var questions: List<Question> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(view)
        setupFinishButton(view)
        fetchQuestions()
    }

    private fun setupRecyclerView(view: View) {
        questionsRecyclerView = view.findViewById(R.id.questions_recycler_view)
        questionAdapter = QuestionAdapter { position, selectedAnswer ->
            handleAnswer(position, selectedAnswer)
        }

        questionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = questionAdapter
        }
    }

    private fun setupFinishButton(view: View) {
        finishButton = view.findViewById(R.id.finish_button)
        finishButton.setOnClickListener {
            if (answeredCount < questions.size) {
                Toast.makeText(
                    context,
                    "Please answer all questions before finishing",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                navigateToScore()
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

    private fun fetchQuestions() {
        RetrofitInstance.api.getQuestions(categoryId).enqueue(object : Callback<TriviaResponse> {
            override fun onResponse(call: Call<TriviaResponse>, response: Response<TriviaResponse>) {
                if (response.isSuccessful) {
                    questions = response.body()?.results ?: emptyList()
                    questionAdapter.submitList(questions)
                } else {
                    Toast.makeText(context, "Failed to fetch questions", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TriviaResponse>, t: Throwable) {
                Toast.makeText(context, "Failed to fetch questions", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToScore() {
        val bundle = Bundle().apply {
            putInt("score", score)
            putInt("totalQuestions", questions.size)
            putInt("categoryId", categoryId)
            putString("categoryName", categoryName)
        }
        findNavController().navigate(R.id.action_quiz_to_score, bundle)
    }
}