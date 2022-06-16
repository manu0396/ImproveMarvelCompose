package com.example.newmarvelcompose.domain

import com.example.newmarvelcompose.data.local.MarvelDAO
import com.example.newmarvelcompose.data.local.RoomResponse
import com.example.newmarvelcompose.data.models.MarvelRoom
import com.example.newmarvelcompose.util.WrapperResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepository @Inject constructor(val dao: MarvelDAO) {

    fun insertHero(marvelRoom: MarvelRoom): WrapperResponse<Long> = runBlocking(Dispatchers.IO){
        val response = try{
            dao.insertHero(marvelRoom)
        }catch (ex:Exception){
            return@runBlocking WrapperResponse.Error("Connection to localDatabase failed: Insert")
        }
        return@runBlocking WrapperResponse.Sucess(response)
    }
    fun removeHero(heroToRemove: String): WrapperResponse<Int> = runBlocking(Dispatchers.IO){
        val response = try{
            dao.removeHero(heroToRemove)
        }catch (ex:Exception){
            return@runBlocking WrapperResponse.Error("Connection to localDatabase failed: Delete")
        }
        return@runBlocking WrapperResponse.Sucess(response)
    }
    fun selectLocalHeros(): WrapperResponse<List<RoomResponse>> = runBlocking(Dispatchers.IO){

        val response = try{
            dao.selectHeroBought()
        }catch (ex:Exception){
            return@runBlocking WrapperResponse.Error("Connection to localDatabase failed: Select")
        }

        return@runBlocking WrapperResponse.Sucess(response)
    }

}