package manu.lucas.dev.domain

import manu.lucas.dev.data.remote.MarvelApi
import manu.lucas.dev.data.remote.response.marvel.CharacterDataContainerResponse
import manu.lucas.dev.data.remote.response.marvel.CharacterResultResponse
import manu.lucas.dev.util.WrapperResponse
import javax.inject.Inject
import javax.inject.Singleton


interface MarvelRepository{

    suspend fun getHeroList(limit:Int, offset: Int): WrapperResponse<CharacterDataContainerResponse>

    suspend fun getHeroInfo(heroId:Long): WrapperResponse<CharacterResultResponse>
}