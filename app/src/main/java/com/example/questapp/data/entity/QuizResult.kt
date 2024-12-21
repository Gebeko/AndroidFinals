package com.example.questapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "quiz_results")
data class QuizResult(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val categoryId: Int,
    val categoryName: String,
    val score: Int,
    val totalQuestions: Int,
    val date: Date
)