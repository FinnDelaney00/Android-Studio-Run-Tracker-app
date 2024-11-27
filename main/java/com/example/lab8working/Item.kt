package com.example.lab8working

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var name: String,
    var description: String,
    val category: String,
    val time: String,
    val distance: Float
)