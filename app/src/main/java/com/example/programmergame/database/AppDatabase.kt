package com.example.programmergame.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.programmergame.model.GameValue

@Database(entities = [GameValue::class], version = 4, exportSchema = false)
abstract class AppDatabase:RoomDatabase() {
    companion object{
        private const val DB_NAME = "main.db"
        var db: AppDatabase? = null
        private val LOCK = Any()

        fun getInstance(context: Context):AppDatabase{
            synchronized(LOCK){
                db?.let { return it }
                val instance = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
                db= instance
                return instance
            }
        }

    }
    abstract fun gameDao():GameDao
}