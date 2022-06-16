package com.example.newmarvelcompose

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.newmarvelcompose.data.local.MarvelDAO
import com.example.newmarvelcompose.data.local.RoomResponse
import com.example.newmarvelcompose.data.remote.MarvelApi
import com.example.newmarvelcompose.data.remote.response.marvel.CharacterDataContainerResponse
import com.example.newmarvelcompose.data.remote.response.marvel.CharacterResultResponse
import com.example.newmarvelcompose.data.remote.response.marvel.ComicCharacterResponse
import com.example.newmarvelcompose.data.remote.response.marvel.ThumbnailResponse
import com.example.newmarvelcompose.repository.MarvelRepository
import com.example.newmarvelcompose.repository.MarvelRepository_Factory
import com.example.newmarvelcompose.util.WrapperResponse
import com.example.newmarvelcompose.util.convertPixelsToDp
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import retrofit2.Response

//TODO(): Test the repository

@RunWith(AndroidJUnit4::class)
@SmallTest
class MarvelRepositoryTest {

    @Mock
    private val mockApi = Mockito.mock(MarvelApi::class.java)

    @Mock
    private val mockDao = Mockito.mock(MarvelDAO::class.java)

    private lateinit var repository: MarvelRepository

    private val hero = CharacterResultResponse(
        code = 200,
        status = "OK",
        copyright = "© 2022 MARVEL",
        attributionText = "Data provided by Marvel. © 2022 MARVEL",
            CharacterDataContainerResponse(
                offset = 0,
                limit = 20,
                total = 1,
                count = 1,
                listOf(
                    ComicCharacterResponse(
                        id = 1017100,
                        name = "A-Bomb (HAS)",
                        description = "Rick Jones has been Hulk's best bud since day one, but now he's more than a friend...he's a teammate! Transformed by a Gamma energy explosion, A-Bomb's thick, armored skin is just as strong and powerful as it is blue. And when he curls into action, he uses it like a giant bowling ball of destruction! ",
                        modified = "2013-09-18T15:54:04-0400",
                        thumbnailResponse = ThumbnailResponse(
                            path = "http://i.annihil.us/u/prod/marvel/i/mg/3/20/5232158de5b16",
                            extension = "jpg"
                        ),
                        resourceUri = "http://gateway.marvel.com/v1/public/characters/1017100"
                    )
                )
            )
        )
    private val heroBought = RoomResponse(
        uid = "1017100",
        name = "A-Bomb (HAS)",
        image = "http://i.annihil.us/u/prod/marvel/i/mg/3/20/5232158de5b16.jpg",
        numberId = 1017100,
        bought = 1,
        description = "Rick Jones has been Hulk's best bud since day one, but now he's more than a friend...he's a teammate! Transformed by a Gamma energy explosion, A-Bomb's thick, armored skin is just as strong and powerful as it is blue. And when he curls into action, he uses it like a giant bowling ball of destruction! "
    )

    @Before
    fun setup(){
        MockitoAnnotations.openMocks(this)
        repository = MarvelRepository(mockApi)
    }

    @Test
    fun getAllCountriesFromNetwork() = runBlocking{
        Assert.assertNotNull(mockDao)
        Assert.assertNotNull(mockApi)
        Mockito.`when`(mockDao.selectHeroBought()).thenReturn(listOf())
        Mockito.`when`(mockApi.getCharacters(20, 20)).thenReturn(hero)

        val result = repository.getHeroList(20, 20) as WrapperResponse.Sucess<List<CharacterResultResponse>>

        Assert.assertEquals(1, result.data?.size)
        Assert.assertEquals(hero.dataResponse.results[0].name, result.data?.get(0)?.dataResponse!!.results[0].name)
    }

    //TODO(): Test LocalDatabase


    @Test
    fun getBought() = runBlocking {
        Assert.assertNotNull(mockDao)
        heroBought.bought = 1
        Mockito.`when`(mockDao.selectHeroBought(1)).thenReturn(listOf(heroBought))

        val result = mockDao.selectHeroBought(1) as WrapperResponse.Sucess<List<RoomResponse>>

        Assert.assertEquals(1, result.data?.size)
        Assert.assertEquals(heroBought.name, result.data!![0].name)
        Assert.assertEquals(result.data!![0].bought, 1)
    }
}