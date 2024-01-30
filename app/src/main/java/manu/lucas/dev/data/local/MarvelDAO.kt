package manu.lucas.dev.data.local

import androidx.room.*
import manu.lucas.dev.data.models.MarvelRoom
import manu.lucas.dev.util.Constants

@Dao
interface MarvelDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertHero(marvel: MarvelRoom): Long

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM ${Constants.DATABASE_NAME} WHERE bought=:bought")
    fun selectHeroBought(bought: Int = 1): List<RoomResponse>

    //Return the number of rows delete
    @Query("DELETE FROM ${Constants.DATABASE_NAME} WHERE name=:name")
    fun removeHero(name: String): Int
}