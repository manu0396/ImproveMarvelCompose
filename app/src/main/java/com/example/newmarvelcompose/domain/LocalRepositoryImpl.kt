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
class LocalRepositoryImpl @Inject constructor
    (val dao: MarvelDAO) :LocalRepository
{

    override fun insertHero(marvelRoom: MarvelRoom): WrapperResponse<Long>{
        val response = try{
            dao.insertHero(marvelRoom)
        }catch (ex:Exception){
            return WrapperResponse.Error("Connection to localDatabase failed: Insert")
        }
        return WrapperResponse.Sucess(response)
    }
    override fun removeHero(heroToRemove: String): WrapperResponse<Int> {
        val response = try{
            dao.removeHero(heroToRemove)
        }catch (ex:Exception){
            return WrapperResponse.Error("Connection to localDatabase failed: Delete")
        }
        return WrapperResponse.Sucess(response)
    }
    override fun selectLocalHeros(): WrapperResponse<List<RoomResponse>> {

        val response = try{
            dao.selectHeroBought()
        }catch (ex:Exception){
            return WrapperResponse.Error("Connection to localDatabase failed: Select")
        }

        return WrapperResponse.Sucess(response)
    }

}