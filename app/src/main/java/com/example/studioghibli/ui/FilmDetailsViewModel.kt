package com.example.studioghibli.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studioghibli.data.IFavouriteRepository
import com.example.studioghibli.data.IGhibliRepository
import com.example.studioghibli.model.Film
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

data class FilmDetailsUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val film: Film? = null
)

@HiltViewModel
class FilmDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: IGhibliRepository,
    private val favouriteRepository: IFavouriteRepository
) : ViewModel() {

    private val filmId: String = savedStateHandle["filmId"] ?: ""

    var uiState by mutableStateOf(FilmDetailsUiState(isLoading = true))
        private set

    init {
        loadFilm()
    }

    fun loadFilm() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                val film = repository.getFilmById(filmId)
                uiState = uiState.copy(
                    isLoading = false,
                    film = film,
                    errorMessage = null
                )
            } catch (e: IOException) {
                val favouriteFilm = favouriteRepository.getFavouriteById(filmId)
                if (favouriteFilm != null) {
                    uiState = uiState.copy(isLoading = false, film = favouriteFilm, errorMessage = null)
                } else {
                    uiState = uiState.copy(
                        isLoading = false,
                        errorMessage = "Фильм не сохранен в избранном: ${e.message ?: "Неизвестная ошибка"}"
                    )
                }
            } catch (e: HttpException) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Ошибка сервера: ${e.code()}"
                )
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Неизвестная ошибка"
                )
            }
        }
    }

    fun retry() {
        loadFilm()
    }
}