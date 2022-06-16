package com.example.newmarvelcompose.data.remote.mapper

import com.example.newmarvelcompose.data.models.MarvelListModel
import com.example.newmarvelcompose.data.remote.response.marvel.CharacterDataContainerResponse
import kotlin.collections.ArrayList

//TODO("Adapt to the new API")
class MarvelMapper : ResponseMapper<CharacterDataContainerResponse, List<MarvelListModel>> {

    override fun fromResponse(response: CharacterDataContainerResponse): List<MarvelListModel> {
        val resp: ArrayList<MarvelListModel> = ArrayList()
        
        response.results.mapIndexed { index, comicCharacterResponse ->

            val newHero = MarvelListModel(
                name = comicCharacterResponse.name,
                id = comicCharacterResponse.id.toInt(),
                description = comicCharacterResponse.description,
                thumbnail = "${comicCharacterResponse.thumbnailResponse.path}/portrait_small.${comicCharacterResponse.thumbnailResponse.extension}"
            )
        resp.add(newHero)

        }
        return resp
    }
}