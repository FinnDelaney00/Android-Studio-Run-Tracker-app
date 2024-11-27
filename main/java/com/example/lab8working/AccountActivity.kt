package com.example.lab8working

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountActivity : AppCompatActivity() {

    private lateinit var profilePictureImageView: ImageView
    private lateinit var userNameTextView: TextView
    private lateinit var goalEditText: EditText
    private lateinit var addGoalButton: Button
    private lateinit var goalsRecyclerView: RecyclerView
    private lateinit var goalsAdapter: GoalsAdapter
    private lateinit var goalsDao: GoalsDao
    private lateinit var viewItemsButton: ImageButton
    private lateinit var LoginButton: ImageButton
    private lateinit var AccountButton: ImageButton
    private lateinit var LoginButtonTop: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("AccountActivity", "onCreate called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        profilePictureImageView = findViewById(R.id.profilePictureImageView)
        userNameTextView = findViewById(R.id.userNameTextView)
        goalEditText = findViewById(R.id.goalEditText)
        addGoalButton = findViewById(R.id.addGoalButton)
        goalsRecyclerView = findViewById(R.id.goalsRecyclerView)
        viewItemsButton = findViewById(R.id.viewItemButton)
        LoginButton = findViewById(R.id.profileButton)
        LoginButtonTop = findViewById(R.id.profileButtonTop)
        AccountButton = findViewById(R.id.AccountActivityButton)

        viewItemsButton.setOnClickListener {
            val intent = Intent(this, DisplayActivity::class.java)
            startActivity(intent)
        }

        LoginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        LoginButtonTop.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        AccountButton.setOnClickListener {
            val intent = Intent(this, AccountActivity::class.java)
            startActivity(intent)
        }

            val database = AppDatabase.getDatabase(applicationContext)
            goalsDao = database.GoalsDao()

            goalsRecyclerView.layoutManager = LinearLayoutManager(this)
            goalsAdapter = GoalsAdapter()
            goalsRecyclerView.adapter = goalsAdapter

        // Display a generic username or title
        userNameTextView.text = "All Goals"

        // Load all goals
        CoroutineScope(Dispatchers.IO).launch {
            val goals = goalsDao.getAllGoals()
            withContext(Dispatchers.Main) {
                goalsAdapter.updateGoals(goals)
            }
        }

        // Add goal button click listener (modified)
        addGoalButton.setOnClickListener {
            val goalText = goalEditText.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                // You might need to adjust the Goal entity and database operations
                // if you're removing the user ID association
                goalsDao.insertGoal(Goal(goalText = goalText))
                val updatedGoals = goalsDao.getAllGoals()
                withContext(Dispatchers.Main) {
                    goalsAdapter.updateGoals(updatedGoals)
                }
            }
        }
    }
}