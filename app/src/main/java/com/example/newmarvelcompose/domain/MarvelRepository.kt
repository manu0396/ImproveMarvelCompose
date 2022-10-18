package com.example.newmarvelcompose.domain

import com.example.newmarvelcompose.data.remote.MarvelApi
import com.example.newmarvelcompose.data.remote.response.marvel.CharacterDataContainerResponse
import com.example.newmarvelcompose.data.remote.response.marvel.CharacterResultResponse
import com.example.newmarvelcompose.util.WrapperResponse
import javax.inject.Inject
import javax.inject.Singleton


interface MarvelRepository{

    suspend fun getHeroList(limit:Int, offset: Int): WrapperResponse<CharacterDataContainerResponse>

    suspend fun getHeroInfo(heroId:Long): WrapperResponse<CharacterResultResponse>
}