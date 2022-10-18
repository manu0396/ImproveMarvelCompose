package com.example.newmarvelcompose.ui.herodetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newmarvelcompose.data.models.MarvelListModel
import com.example.newmarvelcompose.data.remote.mapper.MarvelMapper
import com.example.newmarvelcompose.domain.MarvelRepository
import com.example.newmarvelcompose.util.WrapperResponse
import com.example.newmarvelcompose.domain.MarvelRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeroDetailViewModel @Inject constructor(
    private val repositoryImpl: MarvelRepositoryImpl
): ViewModel() {

    private var _loadError : MutableStateFlow<String> = MutableStateFlow("")
    var loadError : StateFlow<String> = _loadError.asStateFlow()
    private var _isLoading :MutableStateFlow<Boolean> = MutableStateFlow(true)
    var isLoading :StateFlow<Boolean> = _isLoading.asStateFlow()
    private var _hero : MutableStateFlow<List<MarvelListModel>> = MutableStateFlow(listOf())
    var hero :StateFlow<List<MarvelListModel>> = _hero.asStateFlow()

    fun getHeroInfo(id : Long) {

        viewModelScope.launch {
            _isLoading.value = true
            val resp = repositoryImpl.getHeroInfo(id)

            when(resp){
                is WrapperResponse.Sucess -> {
                    _hero.value = MarvelMapper().fromResponse(resp.data!!.dataResponse)
                    _loadError.value = ""
                    _isLoading.value = false
                }
                is WrapperResponse.Error -> {
                    _loadError.value = resp.message!!
                    _isLoading.value = false
                }
            }
        }
    }
}