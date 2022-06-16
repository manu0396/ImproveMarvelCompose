package com.example.newmarvelcompose.ui.myHeros

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.newmarvelcompose.data.local.RoomResponse
import com.example.newmarvelcompose.domain.LocalRepository
import com.example.newmarvelcompose.util.WrapperResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MyHerosViewModel @Inject constructor(
    val localRepository: LocalRepository
):
    ViewModel() {

    private var _heroBoughtList : MutableStateFlow<List<RoomResponse>> = MutableStateFlow(listOf())
    var heroBoughtList :StateFlow<List<RoomResponse>> = _heroBoughtList.asStateFlow()
    private var _loadError = MutableStateFlow("")
    var loadError: StateFlow<String> = _loadError.asStateFlow()
    private var _isLoading = MutableStateFlow(false)
    var isLoading : StateFlow<Boolean> = _isLoading.asStateFlow()
    // Initial value is false so the dialog is hidden
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()
    var heroToDelete = mutableStateOf("")


    fun onOpenDialogClicked(hero: String) {
        heroToDelete.value = hero
        _showDialog.value = true

    }

    fun onDialogConfirm(heroToRemove: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _showDialog.value = false
            removeHero(heroToRemove).also {
                getHerosBought()
            }
        }
    }

    fun onDialogDismiss() {
        _showDialog.value = false
    }

    fun getHerosBought() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            var result = localRepository.selectLocalHeros()

            when (result) {
                is WrapperResponse.Sucess -> {

                val pokedexBought = result.data!!.map {
                        RoomResponse(
                            uid = it.uid,
                            name = it.name,
                            image = it.image,
                            numberId = it.numberId,
                            bought = it.bought,
                            description = it.description
                        )
                    }
                    _loadError.value = ""
                    _isLoading.value = false
                    _heroBoughtList.value = pokedexBought
                }
                is WrapperResponse.Error -> {
                    _loadError.value = result.message.toString()
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

    fun removeHero(heroToRemove: String) = runBlocking(Dispatchers.IO){
        _isLoading.value = true
        val resp = localRepository.removeHero(heroToRemove = heroToRemove)

        when(resp){
            is WrapperResponse.Sucess -> {
                //Nº files removed
                if(resp.data != 0){
                    //Número de filas eliminadas
                    _loadError.value = ""
                    _isLoading.value = false
                }else{
                    _loadError.value = "No se ha podido eliminar el hero seleccionado"
                    _isLoading.value = false
                }
            }
            is WrapperResponse.Error -> {
                _loadError.value = resp.message.toString()
                _isLoading.value = false
            }
        }
    }
}