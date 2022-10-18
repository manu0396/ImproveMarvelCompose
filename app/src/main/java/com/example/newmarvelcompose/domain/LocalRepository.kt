package com.example.newmarvelcompose.domain

import com.example.newmarvelcompose.data.local.RoomResponse
import com.example.newmarvelcompose.data.models.MarvelRoom
import com.example.newmarvelcompose.util.WrapperResponse
import javax.inject.Singleton


interface LocalRepository {

    fun insertHero(marvelRoom: MarvelRoom):WrapperResponse<Long>

    fun removeHero(heroToRemove: String): WrapperResponse<Int>

    fun selectLocalHeros():WrapperResponse<List<RoomResponse>>

}