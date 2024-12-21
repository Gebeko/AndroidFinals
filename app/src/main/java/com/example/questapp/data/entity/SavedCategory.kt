package com.example.questapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_categories")
data class SavedCategory(
    @PrimaryKey
    val id: Int,
    val name: String
)