package com.example.lab8working

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditActivity : AppCompatActivity() {

    private lateinit var viewItemsButton: ImageButton
    private lateinit var LoginButton: ImageButton
    private lateinit var AccountButton: ImageButton
    private lateinit var LoginButtonTop: ImageButton
    private lateinit var descriptionEditText: EditText
    private lateinit var itemNameSpinner: Spinner
    private lateinit var itemCategorySpinner: Spinner
    private lateinit var itemDao: ItemDao
    private var itemId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit) // Make sure you have this layout file

        Log.d("EditActivity", "EditEntered")
        itemNameSpinner = findViewById(R.id.itemNameSpinner)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        itemCategorySpinner = findViewById(R.id.itemCategorySpinner)
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

        // Setup spinners (these should be done before fetching data)
        val itemNames = arrayOf("Run", "Walk", "Cycle", "Swim", "Other")
        val itemNameAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, itemNames)
        itemNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        itemNameSpinner.adapter = itemNameAdapter

        val itemCategories = arrayOf("Easy", "Medium", "Hard")
        val itemCategoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, itemCategories)
        itemCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        itemCategorySpinner.adapter = itemCategoryAdapter

        itemDao = AppDatabase.getDatabase(applicationContext).itemDao()

        itemId = intent.getIntExtra("itemId", -1)

        if (itemId != -1) {
            CoroutineScope(Dispatchers.IO).launch {
                val item = itemDao.getItemById(itemId)
                withContext(Dispatchers.Main) {
                    if (item != null) {
                        // Set the current data into the EditText and Spinners
                        descriptionEditText.setText(item.description)

                        // Set the spinner selection based on item data
                        val itemNames = arrayOf("Run", "Walk", "Cycle", "Swim", "Other")
                        itemNameSpinner.setSelection(itemNames.indexOf(item.name))

                        val itemCategories = arrayOf("Easy", "Medium", "Hard")
                        itemCategorySpinner.setSelection(itemCategories.indexOf(item.category))
                    }
                }
            }
        } else {
            Log.e("EditActivity", "Invalid item ID: $itemId")
            finish()
        }

        val saveButton = findViewById<Button>(R.id.saveButton)
        saveButton.setOnClickListener {
            val updatedName = itemNameSpinner.selectedItem.toString()
            val updatedDescription = descriptionEditText.text.toString()
            val updatedCategory = itemCategorySpinner.selectedItem.toString()

            CoroutineScope(Dispatchers.IO).launch {
                val existingItem = itemDao.getItemById(itemId)
                val updatedItem = existingItem?.copy(
                    name = updatedName,
                    description = updatedDescription,
                    category = updatedCategory
                )
                updatedItem?.let { itemDao.update(it) } // Update if not null
                withContext(Dispatchers.Main) {
                    finish()
                }
            }
        }
    }
}
