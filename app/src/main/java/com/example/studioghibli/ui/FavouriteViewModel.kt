package com.example.studioghibli.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studioghibli.data.IFavouriteRepository
import com.example.studioghibli.model.Film
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

data class FavouriteUiState(
    val films: List<Film> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val favouriteRepository: IFavouriteRepository
) : ViewModel() {
    var uiState by mutableStateOf(FavouriteUiState())
        private set

    init {
        loadFavourites()
    }

    fun loadFavourites() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                val films = favouriteRepository.getAllFavourites()
                uiState = uiState.copy(films = films, isLoading = false)
            } catch (e: Exception) {
                val errorText = "Ошибка при загрузке избранного: ${e.message ?: "Неизвестная ошибка"}"
                uiState = uiState.copy(isLoading = false, errorMessage = errorText)
            }
        }
    }

    fun toggleFavourite(film: Film) {
        viewModelScope.launch {
            try {
                if (uiState.films.any { it.id == film.id }) {
                    favouriteRepository.removeFromFavourites(film.id)
                } else {
                    favouriteRepository.addToFavourites(film)
                }
                loadFavourites()
            } catch (e: Exception) {
                val errorText = "Ошибка при загрузке избранного: ${e.message ?: "Неизвестная ошибка"}"
                uiState = uiState.copy(isLoading = false, errorMessage = errorText)
            }
        }
    }

    fun retry() {
        loadFavourites()
    }
}