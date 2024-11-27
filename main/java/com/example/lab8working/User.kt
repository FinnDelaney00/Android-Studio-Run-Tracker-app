package com.example.lab8working

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class User(
    @PrimaryKey var username: String,
    var password: String
)

