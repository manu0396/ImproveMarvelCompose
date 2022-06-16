package com.example.newmarvelcompose.ui.herolist

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.newmarvelcompose.data.models.MarvelListModel
import com.example.newmarvelcompose.data.remote.mapper.MarvelMapper
import com.example.newmarvelcompose.domain.MarvelRepositoryImpl
import com.example.newmarvelcompose.util.Constants.PAGE_SIZE
import com.example.newmarvelcompose.util.WrapperResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeroListViewModel @Inject constructor(
    private val repositoryImpl: MarvelRepositoryImpl
) : ViewModel() {

    private var curPage = 0
    private var _heroList : MutableStateFlow<List<MarvelListModel>> = MutableStateFlow(listOf())
    var heroList: StateFlow<List<MarvelListModel>> = _heroList.asStateFlow()
    private var _loadError = MutableStateFlow("")
    var loadError = _loadError.asStateFlow()
    private var _isLoading = MutableStateFlow(false)
    var isLoading = _isLoading.asStateFlow()

    private var _endReached = MutableStateFlow(false)
    var endReached:StateFlow<Boolean> = _endReached.asStateFlow()

    private var boughtList = listOf<MarvelListModel>()

    private var isSearchStarting = true
    var isSearching = mutableStateOf(false)

    init {
        loadHeroPaginated()
    }

    fun searchheroList(query: String) {
        val listToSearch = if (isSearchStarting) {
            _heroList.value
        } else {
            boughtList
        }

        viewModelScope.launch(Dispatchers.Default) {

            if (query.isEmpty()) {
                _heroList.value = boughtList
                isSearching.value = false
                isSearchStarting = true
                return@launch
            }

            val results = listToSearch.filter {
                it.name.contains(query.trim(), ignoreCase = true) ||
                        it.id.toString() == query.trim()
            }

            if (isSearchStarting) {
                boughtList = _heroList.value
                isSearchStarting = false
            }

            _heroList.value = results
            isSearching.value = true

        }
    }

    //TODO("Check the response")
    fun loadHeroPaginated() {

        viewModelScope.launch {
            _isLoading.value = true
            val result = repositoryImpl.getHeroList(PAGE_SIZE, offset = curPage * PAGE_SIZE)
            when (result) {
                is WrapperResponse.Sucess -> {
                    _endReached.value = curPage * PAGE_SIZE >= result.data!!.count
                    val HerosEntries = MarvelMapper().fromResponse(result.data)

                    curPage++
                    _loadError.value = ""
                    _isLoading.value = false
                    _heroList.value += HerosEntries
                }
                is WrapperResponse.Error -> {

                    _loadError.value = result.message!!
                    _isLoading.value = false

                }
            }
        }
    }

    fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }

}