package com.example.newmarvelcompose.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.newmarvelcompose.util.Constants

//NumberId is the Id return by the API.
@Entity(tableName = Constants.DATABASE_NAME, indices = [Index(value = ["uid"], unique = true)])
data class MarvelRoom(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    var uid:String  = id.toString(),
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "image") val image:String,
    @ColumnInfo(name = "numberId") val numberId: Int,
    @ColumnInfo(name = "bought") var bought: Int = 0
)
