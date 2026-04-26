package com.example.studioghibli.ui

import androidx.lifecycle.SavedStateHandle
import com.example.studioghibli.MainDispatcherRule
import com.example.studioghibli.data.FavouriteRepository
import com.example.studioghibli.data.GhibliRepository
import com.example.studioghibli.model.Film
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class FilmDetailsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var ghibliRepository: GhibliRepository
    private lateinit var favouriteRepository: FavouriteRepository
    private lateinit var savedStateHandle: SavedStateHandle

    private val film = Film(
        "1",
        "Howl's Moving Castle",
        "ハウルの動く城",
        "The best",
        "Hayao Miyazaki",
        "Toshio Suzuki",
        "2004",
        119,
        "87"
    )

    @Before
    fun setup() {
        ghibliRepository = mockk()
        favouriteRepository = mockk()
        savedStateHandle = SavedStateHandle(mapOf("filmId" to "1"))
    }

    @Test
    fun `savedStateHandle correct filmId`() {
        coEvery { ghibliRepository.getFilmById("1") } returns film
        coEvery { favouriteRepository.getFavouriteById("1") } returns null
        val viewModel = FilmDetailsViewModel(savedStateHandle, ghibliRepository, favouriteRepository)
        val filmIdField = FilmDetailsViewModel::class.java.getDeclaredField("filmId")
        filmIdField.isAccessible = true
        val actualId = filmIdField.get(viewModel) as String
        assertEquals("1", actualId)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadFilm network error then loads from favourites`() = runTest {
        coEvery { ghibliRepository.getFilmById("1") } throws IOException("Нет интернета")
        coEvery { favouriteRepository.getFavouriteById("1") } returns film
        val viewModel = FilmDetailsViewModel(savedStateHandle, ghibliRepository, favouriteRepository)
        advanceUntilIdle()

        assertFalse(viewModel.uiState.isLoading)
        assertNotNull(viewModel.uiState.film)
        assertEquals(film.title, viewModel.uiState.film!!.title)
        assertNull(viewModel.uiState.errorMessage)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadFilm shows error`() = runTest {
        coEvery { ghibliRepository.getFilmById("1") } throws IOException("Нет интернета")
        coEvery { favouriteRepository.getFavouriteById("1") } returns null
        val viewModel = FilmDetailsViewModel(savedStateHandle, ghibliRepository, favouriteRepository)
        advanceUntilIdle()

        assertFalse(viewModel.uiState.isLoading)
        assertNull(viewModel.uiState.film)
        assertNotNull(viewModel.uiState.errorMessage)
        assertTrue(viewModel.uiState.errorMessage!!.contains("Нет интернета"))
    }

}