package com.example.newmarvelcompose

import androidx.lifecycle.ViewModel
import com.example.newmarvelcompose.data.models.MarvelRoom
import com.example.newmarvelcompose.domain.LocalRepository
import com.example.newmarvelcompose.util.WrapperResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val localRepository: LocalRepository
    ): ViewModel() {

    fun insertHeroBought(marvel: MarvelRoom): WrapperResponse<Long> = runBlocking(Dispatchers.IO){

        return@runBlocking localRepository.insertHero(marvelRoom = marvel)
    }

}