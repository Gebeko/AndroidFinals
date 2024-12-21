package com.example.questapp.api

import android.text.Html
import com.example.questapp.data.CategoryResponse
import com.example.questapp.data.TriviaResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.lang.reflect.Type

object RetrofitInstance {
    private const val BASE_URL = "https://opentdb.com/"

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(String::class.java, HtmlEntityDecoder())
        .create()

    val api: TriviaApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
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

class HtmlEntityDecoder : JsonDeserializer<String> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: com.google.gson.JsonDeserializationContext?): String {
        return if (json != null) {
            Html.fromHtml(json.asString).toString()
        } else {
            ""
        }
    }
}