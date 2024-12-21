package com.example.questapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.questapp.R
import com.example.questapp.adapter.CategoryAdapter
import com.example.questapp.api.RetrofitInstance
import com.example.questapp.data.AppDatabase
import com.example.questapp.data.Category
import com.example.questapp.data.CategoryResponse
import com.example.questapp.repository.QuizRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategorySelectionFragment : Fragment() {
    private lateinit var categoriesRecyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var quizRepository: QuizRepository
    private var categories: List<Category> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        quizRepository = QuizRepository(
            AppDatabase.getDatabase(requireContext()).quizDao(),
            AppDatabase.getDatabase(requireContext()).categoryDao()
        )
        setupRecyclerView()
        fetchCategories()
    }
    private fun setupRecyclerView() {
        categoriesRecyclerView = view?.findViewById(R.id.categories_recycler_view)!!
        categoryAdapter = CategoryAdapter(categories) { selectedCategory ->
            val bundle = Bundle().apply {
                putInt("categoryId", selectedCategory.id)
                putString("categoryName", selectedCategory.name)
            }
            findNavController().navigate(R.id.action_categorySelection_to_quiz, bundle)
        }
        categoriesRecyclerView?.adapter = categoryAdapter
    }
    private fun fetchCategories() {
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryResponse> {
            override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                if (response.isSuccessful) {
                    categories = response.body()?.trivia_categories ?: emptyList()
                    categoryAdapter = CategoryAdapter(categories) { selectedCategory ->
                        val bundle = Bundle().apply {
                            putInt("categoryId", selectedCategory.id)
                        }
                        findNavController().navigate(R.id.action_categorySelection_to_quiz, bundle)
                    }
                    categoriesRecyclerView.adapter = categoryAdapter
                } else {
                    Toast.makeText(context, "Failed to load categories", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                Toast.makeText(context, "Failed to load categories", Toast.LENGTH_SHORT).show()
            }
        })
    }
}