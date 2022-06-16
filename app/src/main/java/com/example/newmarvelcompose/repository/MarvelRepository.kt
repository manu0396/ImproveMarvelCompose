package com.example.newmarvelcompose.repository

import com.example.newmarvelcompose.data.remote.MarvelApi
import com.example.newmarvelcompose.data.remote.response.marvel.CharacterDataContainerResponse
import com.example.newmarvelcompose.data.remote.response.marvel.CharacterResultResponse
import com.example.newmarvelcompose.util.WrapperResponse
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MarvelRepository @Inject constructor(
    private val api: MarvelApi
) {

    suspend fun getHeroList(limit:Int, offset:Int): WrapperResponse<CharacterDataContainerResponse> {
        val response = try{
            api.getCharacters(limit,offset)
        }catch(e:Exception){
            return WrapperResponse.Error("An Unknown error occured.")
        }
      return WrapperResponse.Sucess(response.dataResponse)
    }


    suspend fun getHeroInfo(heroId:Long): WrapperResponse<CharacterResultResponse> {
        val response = try{
            api.getCharacterById(heroId)
        }catch(e:Exception){
            return WrapperResponse.Error("An Unknown error occured.")
        }
        return WrapperResponse.Sucess(response)
    }

}