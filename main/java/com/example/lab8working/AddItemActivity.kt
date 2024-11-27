package com.example.lab8working

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.lab8working.R.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddItemActivity : AppCompatActivity() {
    private lateinit var itemDao: ItemDao
    private lateinit var descriptionEditText: EditText
    private lateinit var itemNameSpinner: Spinner
    private lateinit var itemCategorySpinner: Spinner
    private lateinit var itemTimeTextView: TextView
    private lateinit var itemDistanceTextView: TextView
    private lateinit var viewItemsButton: ImageButton
    private lateinit var LoginButton: ImageButton
    private lateinit var AccountButton: ImageButton
    private lateinit var LoginButtonTop: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_add_item)

        itemDao = AppDatabase.getDatabase(applicationContext).itemDao()
        itemNameSpinner = findViewById(id.itemNameSpinner)
        descriptionEditText = findViewById(id.descriptionEditText)
        itemCategorySpinner = findViewById(id.itemCategorySpinner)
        itemTimeTextView = findViewById(id.itemTimeTextView)
        itemDistanceTextView = findViewById(id.itemDistanceTextView)
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

        val saveButton = findViewById<Button>(id.saveButton)
        val lastSeenTime = intent.getStringExtra("Activity Duration") ?: ""
        val distance = intent.getFloatExtra("Activity Distance", 0f)
        Log.d("FinnRuns", "$distance is being passed")
        //had error passing data. logs were used to help find cause
        Log.d("AddItemActivity", "Retrieved time data: $lastSeenTime")

        val itemNames = arrayOf("Run", "Walk", "Cycle", "Swim", "Other")
        val itemNameAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, itemNames)
        itemNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        itemNameSpinner.adapter = itemNameAdapter

        val itemCategories = arrayOf("Easy", "Medium", "Hard")
        val itemCategoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, itemCategories)
        itemCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        itemCategorySpinner.adapter = itemCategoryAdapter

        val itemTimeTextView: TextView = findViewById(R.id.itemTimeTextView)
        itemTimeTextView.text = lastSeenTime
        //had error passing data. logs were used to help find cause
        Log.d("AddItemActivity", "Settingtime text to TextView: $lastSeenTime")

        if (lastSeenTime != null) {
            itemTimeTextView.text = "Time: $lastSeenTime"
        }

        itemDistanceTextView.text = "Distance: $distance meters"

        saveButton.setOnClickListener {
            val selectedItemName = itemNameSpinner.selectedItem.toString()
            val description = descriptionEditText.text.toString()
            val selectedCategory = itemCategorySpinner.selectedItem.toString()
            val time = lastSeenTime
            val distance = distance

            val Item = Item(
                name = selectedItemName,
                description = description,
                category = selectedCategory,
                time = time,
                distance = distance
            )
            CoroutineScope(Dispatchers.IO).launch {
                itemDao.insert(Item)

            }
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }}}
