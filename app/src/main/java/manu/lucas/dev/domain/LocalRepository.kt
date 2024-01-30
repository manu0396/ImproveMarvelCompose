package manu.lucas.dev.domain

import manu.lucas.dev.data.local.RoomResponse
import manu.lucas.dev.data.models.MarvelRoom
import manu.lucas.dev.util.WrapperResponse
import javax.inject.Singleton


interface LocalRepository {

    fun insertHero(marvelRoom: MarvelRoom): WrapperResponse<Long>

    fun removeHero(heroToRemove: String): WrapperResponse<Int>

    fun selectLocalHeros(): WrapperResponse<List<RoomResponse>>

}