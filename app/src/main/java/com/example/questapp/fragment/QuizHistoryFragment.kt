package com.example.questapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.questapp.R
import com.example.questapp.adapter.QuizHistoryAdapter
import com.example.questapp.data.AppDatabase
import com.example.questapp.repository.QuizRepository
import kotlinx.coroutines.launch

class QuizHistoryFragment : Fragment() {
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var quizRepository: QuizRepository
    private lateinit var historyAdapter: QuizHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_quiz_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        quizRepository = QuizRepository(
            AppDatabase.getDatabase(requireContext()).quizDao(),
            AppDatabase.getDatabase(requireContext()).categoryDao()
        )

        setupRecyclerView(view)
        observeQuizHistory()
    }

    private fun setupRecyclerView(view: View) {
        historyRecyclerView = view.findViewById(R.id.history_recycler_view)
        historyAdapter = QuizHistoryAdapter()

        historyRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = historyAdapter
        }
    }

    private fun observeQuizHistory() {
        viewLifecycleOwner.lifecycleScope.launch {
            quizRepository.allQuizResults.collect { quizResults ->
                historyAdapter.submitList(quizResults)
            }
        }
    }
}