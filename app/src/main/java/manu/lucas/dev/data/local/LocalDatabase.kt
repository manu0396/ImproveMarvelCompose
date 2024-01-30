package manu.lucas.dev.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import manu.lucas.dev.data.models.MarvelRoom
import manu.lucas.dev.util.Constants

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