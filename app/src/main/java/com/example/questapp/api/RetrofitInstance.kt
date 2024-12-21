package com.example.questapp.api

import com.example.questapp.data.CategoryResponse
import com.example.questapp.data.TriviaResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object RetrofitInstance {
    private const val BASE_URL = "https://opentdb.com/"

    val api: TriviaApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TriviaApi::class.java)
    }
}

interface TriviaApi {
    @GET("api.php?amount=10")
    fun getQuestions(@Query("category") category: Int): Call<TriviaResponse>

    @GET("api_category.php")
    fun getCategories(): Call<CategoryResponse>
}