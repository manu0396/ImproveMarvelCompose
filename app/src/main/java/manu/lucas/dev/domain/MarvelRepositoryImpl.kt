package manu.lucas.dev.domain

import manu.lucas.dev.data.remote.MarvelApi
import manu.lucas.dev.data.remote.response.marvel.CharacterDataContainerResponse
import manu.lucas.dev.data.remote.response.marvel.CharacterResultResponse
import manu.lucas.dev.util.WrapperResponse
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MarvelRepositoryImpl @Inject constructor(
    private val api: MarvelApi,
): MarvelRepository {

    override suspend fun getHeroList(limit:Int, offset:Int): WrapperResponse<CharacterDataContainerResponse> {
        val response = try{
            api.getCharacters(limit,offset)
        }catch(e:Exception){
            return WrapperResponse.Error("An Unknown error occured.")
        }
      return WrapperResponse.Sucess(response.dataResponse)
    }


    override suspend fun getHeroInfo(heroId:Long): WrapperResponse<CharacterResultResponse> {
        val response = try{
            api.getCharacterById(heroId)
        }catch(e:Exception){
            return WrapperResponse.Error("An Unknown error occured.")
        }
        return WrapperResponse.Sucess(response)
    }

}