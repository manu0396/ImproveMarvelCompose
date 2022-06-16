package com.example.newmarvelcompose.data.local

import androidx.room.*
import com.example.newmarvelcompose.data.models.MarvelRoom
import com.example.newmarvelcompose.util.Constants

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