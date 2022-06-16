package com.example.newmarvelcompose.domain

import com.example.newmarvelcompose.data.remote.response.marvel.CharacterDataContainerResponse
import com.example.newmarvelcompose.data.remote.response.marvel.CharacterResultResponse
import com.example.newmarvelcompose.util.WrapperResponse

interface MarvelRepository {

    suspend fun getHeroList(limit:Int, offset: Int): WrapperResponse<CharacterDataContainerResponse>

    suspend fun getHeroInfo(heroId:Long): WrapperResponse<CharacterResultResponse>
}