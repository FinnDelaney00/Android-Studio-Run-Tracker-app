package com.example.lab8working

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.activity.result.ActivityResultLauncher

class DisplayActivity : AppCompatActivity() {

    private lateinit var itemDao: ItemDao
    private lateinit var addItemLauncher: ActivityResultLauncher<Intent>
    private lateinit var adapter: ItemAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewItemsButton: ImageButton
    private lateinit var LoginButton: ImageButton
    private lateinit var AccountButton: ImageButton
    private lateinit var LoginButtonTop: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_layout)

        adapter = ItemAdapter(emptyList())
        itemDao = AppDatabase.getDatabase(applicationContext).itemDao()
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        viewItemsButton = findViewById(R.id.viewItemButton)
        LoginButton = findViewById(R.id.profileButton)
        LoginButtonTop = findViewById(R.id.profileButtonTop)
        AccountButton = findViewById(R.id.AccountActivityButton)

        addItemLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                loadItems()
            }
        }

        loadItems()


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

    private fun loadItems() {
        CoroutineScope(Dispatchers.IO).launch {
            val items = itemDao.getAllItems()
            withContext(Dispatchers.Main) {
                adapter = ItemAdapter(items) // Remove lambda expression
                recyclerView.adapter = adapter
            }
        }
    }
}
//    private fun loadItems() {
//        CoroutineScope(Dispatchers.IO).launch {
//            val items = itemDao.getAllItems()
//            withContext(Dispatchers.Main) {
//                adapter = ItemAdapter(items, this@DisplayActivity) { item ->
//                    val intent = Intent(this@DisplayActivity, AddItemActivity::class.java).apply {
//                        putExtra("ITEM_ID", item.id)
//                    }
//                    addItemLauncher.launch(intent)
//                }
//                recyclerView.adapter = adapter
//            }
//        }
//    }
//}


