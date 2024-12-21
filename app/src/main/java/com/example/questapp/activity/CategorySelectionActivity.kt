package com.example.questapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.questapp.MainActivity
import com.example.questapp.R
import com.example.questapp.adapter.CategoryAdapter
import com.example.questapp.api.RetrofitInstance
import com.example.questapp.data.Category
import com.example.questapp.data.CategoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategorySelectionActivity : AppCompatActivity() {
    private lateinit var categoriesRecyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
    private var categories: List<Category> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_selection)

        categoriesRecyclerView = findViewById(R.id.categories_recycler_view)

        fetchCategories()
    }

    private fun fetchCategories() {
        val api = RetrofitInstance.api
        api.getCategories().enqueue(object : Callback<CategoryResponse> {
            override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                if (response.isSuccessful) {
                    categories = response.body()?.trivia_categories ?: emptyList()
                    categoryAdapter = CategoryAdapter(categories) { selectedCategory ->
                        val intent = Intent(this@CategorySelectionActivity, MainActivity::class.java)
                        intent.putExtra("categoryId", selectedCategory.id)
                        startActivity(intent)
                        finish()
                    }
                    categoriesRecyclerView.adapter = categoryAdapter
                } else {
                    Toast.makeText(this@CategorySelectionActivity, "Failed to load categories", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                Toast.makeText(this@CategorySelectionActivity, "Failed to load categories", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
