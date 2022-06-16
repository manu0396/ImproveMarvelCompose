package com.example.newmarvelcompose.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.newmarvelcompose.data.models.MarvelRoom
import com.example.newmarvelcompose.util.Constants

@Database(entities = [MarvelRoom::class], version = 1, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun dao(): MarvelDAO

    companion object{
        private var INSTANCE : LocalDatabase? = null
        fun getInstance(context: Context): LocalDatabase {
            if(INSTANCE ==null){
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        LocalDatabase::class.java,
                        Constants.DATABASE_NAME
                    )
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}