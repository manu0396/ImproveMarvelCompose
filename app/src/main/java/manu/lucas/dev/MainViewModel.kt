package manu.lucas.dev

import androidx.lifecycle.ViewModel
import manu.lucas.dev.data.models.MarvelRoom
import manu.lucas.dev.domain.LocalRepositoryImpl
import manu.lucas.dev.util.WrapperResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val localRepositoryImpl: LocalRepositoryImpl
    ): ViewModel() {

    fun insertHeroBought(marvel: MarvelRoom): WrapperResponse<Long> = runBlocking(Dispatchers.IO){

        return@runBlocking localRepositoryImpl.insertHero(marvelRoom = marvel)
    }

}