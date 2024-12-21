package com.example.questapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.questapp.R
import com.example.questapp.data.AppDatabase
import com.example.questapp.data.entity.QuizResult
import com.example.questapp.repository.QuizRepository
import kotlinx.coroutines.launch
import java.util.Date

class ScoreFragment : Fragment() {
    private lateinit var quizRepository: QuizRepository
    private val score: Int by lazy {
        arguments?.getInt("score", 0) ?: 0
    }
    private val totalQuestions: Int by lazy {
        arguments?.getInt("totalQuestions", 0) ?: 0
    }
    private val categoryId: Int by lazy {
        arguments?.getInt("categoryId", 0) ?: 0
    }
    private val categoryName: String by lazy {
        arguments?.getString("categoryName", "") ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_score, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        quizRepository = QuizRepository(
            AppDatabase.getDatabase(requireContext()).quizDao(),
            AppDatabase.getDatabase(requireContext()).categoryDao()
        )

        // Save quiz result
        lifecycleScope.launch {
            val quizResult = QuizResult(
                categoryId = categoryId,
                categoryName = categoryName,
                score = score,
                totalQuestions = totalQuestions,
                date = Date()
            )
            quizRepository.insertQuizResult(quizResult)
        }
        val scoreMessage: TextView = view.findViewById(R.id.score_message)
        val retryButton: Button = view.findViewById(R.id.retry_button)
        val exitButton: Button = view.findViewById(R.id.exit_button)
        val viewHistoryButton: Button = view.findViewById(R.id.viewHistoryButton)

        val grade = (score.toDouble() / totalQuestions) * 100
        val message = "You answered $score out of $totalQuestions questions correctly.\n" +
                "Your grade: ${"%.2f".format(grade)}%"

        scoreMessage.text = message

        retryButton.setOnClickListener {
            findNavController().navigate(R.id.action_score_to_categorySelection)
        }

        exitButton.setOnClickListener {
            requireActivity().finish()
        }

        viewHistoryButton.setOnClickListener {
            findNavController().navigate(R.id.action_score_to_history)
        }
    }
}