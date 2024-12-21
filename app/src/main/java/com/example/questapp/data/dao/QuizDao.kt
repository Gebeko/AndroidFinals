package com.example.questapp.data.dao

import androidx.room.*
import com.example.questapp.data.entity.QuizResult
import com.example.questapp.data.entity.SavedCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {
    @Insert
    suspend fun insertQuizResult(quizResult: QuizResult)

    @Query("SELECT * FROM quiz_results ORDER BY date DESC")
    fun getAllQuizResults(): Flow<List<QuizResult>>

    @Query("SELECT * FROM quiz_results WHERE categoryId = :categoryId ORDER BY date DESC")
    fun getQuizResultsByCategory(categoryId: Int): Flow<List<QuizResult>>

    @Query("SELECT AVG(CAST(score AS FLOAT) / totalQuestions * 100) FROM quiz_results")
    fun getAverageScore(): Flow<Float>
}

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<SavedCategory>)

    @Query("SELECT * FROM saved_categories")
    fun getAllCategories(): Flow<List<SavedCategory>>
}
