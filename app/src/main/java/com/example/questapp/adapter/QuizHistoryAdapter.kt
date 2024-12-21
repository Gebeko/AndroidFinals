package com.example.questapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.questapp.R
import com.example.questapp.data.entity.QuizResult
import java.text.SimpleDateFormat
import java.util.Locale

class QuizHistoryAdapter : ListAdapter<QuizResult, QuizHistoryAdapter.HistoryViewHolder>(HistoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_quiz_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryText: TextView = itemView.findViewById(R.id.category_text)
        private val scoreText: TextView = itemView.findViewById(R.id.score_text)
        private val dateText: TextView = itemView.findViewById(R.id.date_text)

        fun bind(quizResult: QuizResult) {
            categoryText.text = quizResult.categoryName
            scoreText.text = "${quizResult.score}/${quizResult.totalQuestions}"
            dateText.text = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                .format(quizResult.date)
        }
    }

    class HistoryDiffCallback : DiffUtil.ItemCallback<QuizResult>() {
        override fun areItemsTheSame(oldItem: QuizResult, newItem: QuizResult): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: QuizResult, newItem: QuizResult): Boolean {
            return oldItem == newItem
        }
    }
}