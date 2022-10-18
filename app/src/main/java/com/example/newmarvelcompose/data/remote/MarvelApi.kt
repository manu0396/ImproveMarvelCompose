package com.example.newmarvelcompose.data.remote

import com.example.newmarvelcompose.data.remote.response.marvel.CharacterResultResponse
import com.example.newmarvelcompose.data.remote.response.marvel.ComicDataWrapperResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelApi {

    @GET("v1/public/characters")
    suspend fun getCharacters(
        @Query("limit") limit:Int,
        @Query("offset") offset:Int,
    ): CharacterResultResponse

    @GET("v1/public/characters/{characterId}")
    suspend fun getCharacterById(
        @Path("characterId") characterId: Long
    ): CharacterResultResponse

}