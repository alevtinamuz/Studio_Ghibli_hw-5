package com.example.studioghibli.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studioghibli.data.FavouriteRepository
import com.example.studioghibli.data.GhibliRepository
import com.example.studioghibli.data.IFavouriteRepository
import com.example.studioghibli.data.IGhibliRepository
import com.example.studioghibli.model.Film
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

data class FilmListUiState(
    val searchQuery: String = "",
    val films: List<Film> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

@HiltViewModel
class FilmViewModel @Inject constructor(
    private val ghibliRepository: IGhibliRepository,
    private val favouriteRepository: IFavouriteRepository
) : ViewModel() {
    var uiState by mutableStateOf(FilmListUiState())
        private set

    private var allFilms: List<Film> = emptyList()
    private var favouriteIds = setOf<String>()

    init {
        loadAllFilms()
    }

    private suspend fun loadFavourites() {
        favouriteIds = favouriteRepository.getAllFavourites().map { it.id }.toSet()
    }

    private fun loadAllFilms() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                val films = ghibliRepository.getFilms()
                allFilms = films
                loadFavourites()
                applyFilter()
                uiState = uiState.copy(isLoading = false)
            } catch (e: Exception) {
                val errorText = when (e) {
                    is IOException -> "Ошибка сети. Проверьте подключение."
                    is HttpException -> "Ошибка сервера: ${e.code()}."
                    else -> e.message ?: "Неизвестная ошибка."
                }
                uiState = uiState.copy(isLoading = false, errorMessage = errorText)
            }
        }
    }

    fun updateSearchQuery(query: String) {
        uiState = uiState.copy(searchQuery = query)
        applyFilter()
    }

    private fun applyFilter() {
        val query = uiState.searchQuery.trim()
        val filtered = allFilms
            .filter { film ->
                query.isBlank() || film.title.contains(query, ignoreCase = true)
            }
            .map { film ->
                film.copy(isFavourite = favouriteIds.contains(film.id))
            }
        uiState = uiState.copy(films = filtered)
    }

    fun toggleFavourite(film: Film) {
        viewModelScope.launch {
            try {
                if (favouriteIds.contains(film.id)) {
                    favouriteRepository.removeFromFavourites(film.id)
                    favouriteIds = favouriteIds.minus(film.id)
                } else {
                    favouriteRepository.addToFavourites(film)
                    favouriteIds = favouriteIds.plus(film.id)
                }
                applyFilter()
                uiState = uiState.copy(errorMessage = null)
            } catch (e: Exception) {
                val errorText = "Ошибка при изменении избранного: ${e.message ?: "Неизвестная ошибка"}"
                uiState = uiState.copy(errorMessage = errorText)
            }
        }
    }

    fun retry() {
        loadAllFilms()
    }

    fun refreshFavourites() {
        viewModelScope.launch {
            loadFavourites()
            applyFilter()
        }
    }
}