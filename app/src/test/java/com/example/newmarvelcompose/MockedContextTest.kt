package com.example.newmarvelcompose

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.newmarvelcompose.data.local.MarvelDAO
import com.example.newmarvelcompose.data.remote.MarvelApi
import com.example.newmarvelcompose.data.remote.response.marvel.ComicCharacterResponse
import com.example.newmarvelcompose.data.remote.response.marvel.ThumbnailResponse
import com.example.newmarvelcompose.repository.MarvelRepository
import com.example.newmarvelcompose.repository.MarvelRepository_Factory
import com.example.newmarvelcompose.util.WrapperResponse
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

    private val hero = ComicCharacterResponse(
        id = 1,
        name = "Spiderman",
        description = "Super human with spider power's",
        modified = "modified example",
        resourceUri = "resourceUriExample",
        thumbnailResponse = ThumbnailResponse(
            path =
        )
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
        Mockito.`when`(mockDao.getAllCountries()).thenReturn(listOf())
        Mockito.`when`(mockApi.getAllCountries()).thenReturn(Response.success(listOf(country)))

        val result = repository.getAllCountries() as AppResult.Success<List<Country>>

        Assert.assertEquals(1, result.successData.size)
        Assert.assertEquals(country.name, result.successData[0].name)
    }

    @Test
    fun getAllCountriesFromDatabase() = runBlocking {
        Assert.assertNotNull(mockDao)
        Mockito.`when`(mockDao.getAllCountries()).thenReturn(listOf(country))

        val result = repository.getAllCountries() as AppResult.Success<List<Country>>

        Assert.assertEquals(1, result.successData.size)
        Assert.assertEquals(country.name, result.successData[0].name)
    }

    @Test
    fun updateFavourite() = runBlocking{
        Assert.assertNotNull(mockDao)
        Mockito.`when`(mockDao.updateFavourite(any(Int::class.java), any(Boolean::class.java)))

        val result = repository.updateFavourite(1, true) as AppResult.Success<Boolean>

        Assert.assertTrue(result.successData)
    }

    @Test
    fun getFavourites() = runBlocking {
        Assert.assertNotNull(mockDao)
        country.isFavourite = true
        Mockito.`when`(mockDao.getFavourites(any(Boolean::class.java))).thenReturn(listOf(country))

        val result = repository.getFavourites() as WrapperResponse.Success<List<Country>>

        Assert.assertEquals(1, result.successData.size)
        Assert.assertEquals(country.name, result.successData[0].name)
        Assert.assertTrue(result.successData[0].isFavourite)
    }
}