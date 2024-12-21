package com.example.questapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.questapp.MainActivity
import com.example.questapp.R

class ScoreActivity : AppCompatActivity() {

    private lateinit var scoreMessage: TextView
    private lateinit var retryButton: Button
    private lateinit var exitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        scoreMessage = findViewById(R.id.score_message)
        retryButton = findViewById(R.id.retry_button)
        exitButton = findViewById(R.id.exit_button)

        val score = intent.getIntExtra("score", 0)
        val totalQuestions = intent.getIntExtra("totalQuestions", 0)
        val grade = (score.toDouble() / totalQuestions) * 100
        val message = "You answered $score out of $totalQuestions questions correctly.\n" +
                "Your grade: ${"%.2f".format(grade)}%"

        scoreMessage.text = message

        retryButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        exitButton.setOnClickListener {
            finish()
        }
    }
}
