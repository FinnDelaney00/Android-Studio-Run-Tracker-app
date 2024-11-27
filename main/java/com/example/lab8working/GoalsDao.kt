package com.example.lab8working

import androidx.room.*

@Dao
interface GoalsDao {


    @Insert
    suspend fun insertGoal(goal: Goal)

    @Delete
    suspend fun deleteGoal(goal: Goal)

    @Query("SELECT * FROM goals")
    suspend fun getAllGoals(): List<Goal>
}