package com.example.questapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.questapp.R
import com.example.questapp.data.Question

class QuestionAdapter(
    private val onAnswerSubmitted: (Int, String) -> Unit
) : ListAdapter<Question, QuestionAdapter.QuestionViewHolder>(QuestionDiffCallback()) {

    private val answeredQuestions = mutableSetOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.question_item, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val questionText: TextView = itemView.findViewById(R.id.question_text)
        private val optionsGroup: RadioGroup = itemView.findViewById(R.id.options_group)
        private val submitButton: Button = itemView.findViewById(R.id.submit_button)
        private val blackColor = ContextCompat.getColor(itemView.context, R.color.black)
        fun bind(question: Question, position: Int) {
            questionText.text = question.question
            optionsGroup.removeAllViews()

            // Create and add radio buttons for options
            val options = (question.incorrect_answers + question.correct_answer).shuffled()
            for (option in options) {
                val radioButton = RadioButton(itemView.context)
                radioButton.text = option
                radioButton.setTextColor(blackColor)
                optionsGroup.addView(radioButton)
            }

            // Disable the question if it's already answered
            val isAnswered = position in answeredQuestions
            optionsGroup.isEnabled = !isAnswered
            submitButton.isEnabled = !isAnswered

            submitButton.setOnClickListener {
                val selectedId = optionsGroup.checkedRadioButtonId
                if (selectedId != -1) {
                    val selectedRadioButton = itemView.findViewById<RadioButton>(selectedId)
                    val selectedAnswer = selectedRadioButton.text.toString()
                    onAnswerSubmitted(position, selectedAnswer)
                    answeredQuestions.add(position)
                    optionsGroup.isEnabled = false
                    submitButton.isEnabled = false
                }
            }
        }
    }

    class QuestionDiffCallback : DiffUtil.ItemCallback<Question>() {
        override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean {
            return oldItem.question == newItem.question
        }

        override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean {
            return oldItem == newItem
        }
    }
}