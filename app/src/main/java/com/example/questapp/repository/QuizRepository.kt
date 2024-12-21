package com.example.questapp.repository

import com.example.questapp.data.dao.CategoryDao
import com.example.questapp.data.dao.QuizDao
import com.example.questapp.data.entity.QuizResult
import com.example.questapp.data.entity.SavedCategory
import kotlinx.coroutines.flow.Flow

class QuizRepository(
    private val quizDao: QuizDao,
    private val categoryDao: CategoryDao
) {
    val allQuizResults: Flow<List<QuizResult>> = quizDao.getAllQuizResults()
    val averageScore: Flow<Float> = quizDao.getAverageScore()
    val allCategories: Flow<List<SavedCategory>> = categoryDao.getAllCategories()

    suspend fun insertQuizResult(quizResult: QuizResult) {
        quizDao.insertQuizResult(quizResult)
    }

    suspend fun saveCategories(categories: List<SavedCategory>) {
        categoryDao.insertCategories(categories)
    }

    fun getQuizResultsByCategory(categoryId: Int): Flow<List<QuizResult>> {
        return quizDao.getQuizResultsByCategory(categoryId)
    }
}