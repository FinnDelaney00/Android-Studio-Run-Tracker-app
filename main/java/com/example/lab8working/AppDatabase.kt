package com.example.lab8working

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Create the goals table
        database.execSQL("ALTER TABLE items ADD COLUMN distance REAL NOT NULL DEFAULT 0")

        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `goals` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `goalText` TEXT NOT NULL
            )
            """
        )
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Add the goal_description column
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `goals` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `goalText` TEXT NOT NULL
            )
            """
        )
    }
}
@Database(entities = [Item::class, User::class, Goal::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Abstract function to get the ItemDao instance
    abstract fun itemDao(): ItemDao
    abstract fun UserDao(): UserDao
    abstract fun GoalsDao(): GoalsDao

    // Companion object to hold the singleton instance of AppDatabase
    companion object {
        // Volatile annotation ensures visibility of changes to instance across threads
        @Volatile
        private var instance: AppDatabase? = null

        // Function to get a singleton instance of AppDatabase
        fun getDatabase(context: Context): AppDatabase {
            // If instance is not null, return it; otherwise, create a new instance
            return instance ?: synchronized(this) {
                // Synchronized block to ensure only one instance is created
                val newInstance = Room.databaseBuilder(
                    context.applicationContext, // Pass in the application context
                    AppDatabase::class.java, // Specify the AppDatabase class
                    "app_database" // Set the database name
                ).addMigrations(MIGRATION_1_2)
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build() // Build the Room database
                instance = newInstance // Set the instance to the newly created database
                newInstance // Return the new instance
            }
        }
    }
}
