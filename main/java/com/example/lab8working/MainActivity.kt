package com.example.lab8working

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var itemDao: ItemDao
    private lateinit var startTimerButton: Button
    private lateinit var viewItemsButton: ImageButton
    private lateinit var LoginButton: ImageButton
    private lateinit var AccountButton: ImageButton
    private lateinit var LoginButtonTop: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TAG", "onCreate called before");
        setContentView(R.layout.activity_main)
        Log.d("TAG", "onCreate called after");


        itemDao = AppDatabase.getDatabase(applicationContext).itemDao()
        startTimerButton = findViewById(R.id.startRunButton)
        viewItemsButton = findViewById(R.id.viewItemButton)
        LoginButton = findViewById(R.id.profileButton)
        LoginButtonTop = findViewById(R.id.profileButtonTop)
        AccountButton = findViewById(R.id.AccountActivityButton)

        startTimerButton.setOnClickListener {
            val intent = Intent(this, TimerActivity::class.java)
            startActivity(intent)
        }

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

    }
}