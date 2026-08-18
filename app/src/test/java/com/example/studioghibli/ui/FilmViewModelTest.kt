package com.example.studioghibli.ui

import com.example.studioghibli.MainDispatcherRule
import com.example.studioghibli.data.FavouriteRepository
import com.example.studioghibli.data.GhibliRepository
import com.example.studioghibli.model.Film
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class FilmViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var ghibliRepository: GhibliRepository
    private lateinit var favouriteRepository: FavouriteRepository
    private lateinit var viewModel: FilmViewModel

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
    fun setUp() {
        ghibliRepository = mockk()
        favouriteRepository = mockk()
        coEvery { favouriteRepository.getAllFavourites() } returns emptyList()
        coEvery { ghibliRepository.getFilms() } returns listOf(film)
        viewModel = FilmViewModel(ghibliRepository, favouriteRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `initial state`() = runTest {
        viewModel = FilmViewModel(ghibliRepository, favouriteRepository)

        assertEquals("", viewModel.uiState.searchQuery)
        assertFalse(viewModel.uiState.isLoading)
//        println(viewModel.uiState.films)
        assertTrue(viewModel.uiState.films.isEmpty())
        assertNull(viewModel.uiState.errorMessage)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadAllFilms success updates films`() = runTest {
        advanceUntilIdle()

        assertFalse(viewModel.uiState.isLoading)
        assertEquals(1, viewModel.uiState.films.size)
        assertEquals(film.title, viewModel.uiState.films[0].title)
        assertNull(viewModel.uiState.errorMessage)

        coVerify(exactly = 1) { ghibliRepository.getFilms() }
        coVerify(exactly = 1) { favouriteRepository.getAllFavourites() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadAllFilms network error message`() = runTest {
        coEvery { ghibliRepository.getFilms() } throws IOException("Нет интернета")
        val errorViewModel = FilmViewModel(ghibliRepository, favouriteRepository)
        advanceUntilIdle()

        assertFalse(errorViewModel.uiState.isLoading)
        assertTrue(errorViewModel.uiState.films.isEmpty())
        assertNotNull(errorViewModel.uiState.errorMessage)
        assertTrue(errorViewModel.uiState.errorMessage!!.contains("Ошибка сети"))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `retry after error`() = runTest {
        coEvery { ghibliRepository.getFilms() } throws IOException("Нет интернета")
        val errorViewModel = FilmViewModel(ghibliRepository, favouriteRepository)
        advanceUntilIdle()
        assertNotNull(errorViewModel.uiState.errorMessage)

        coEvery { ghibliRepository.getFilms() } returns listOf(film)
        errorViewModel.retry()
        advanceUntilIdle()

        assertNull(errorViewModel.uiState.errorMessage)
        assertFalse(errorViewModel.uiState.isLoading)
        assertEquals(1, errorViewModel.uiState.films.size)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `search with non-matching query`() = runTest {
        advanceUntilIdle()
        assertEquals(1, viewModel.uiState.films.size)

        viewModel.updateSearchQuery("Film does not exist")
        advanceUntilIdle()

        assertTrue(viewModel.uiState.films.isEmpty())
        assertNull(viewModel.uiState.errorMessage)
        assertFalse(viewModel.uiState.isLoading)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `toggleFavourite adding same film twice`() = runTest {
        advanceUntilIdle()
        val film = viewModel.uiState.films[0]
        assertFalse(film.isFavourite)

        coEvery { favouriteRepository.addToFavourites(film) } returns Unit
        viewModel.toggleFavourite(film)
        advanceUntilIdle()
        assertTrue(viewModel.uiState.films[0].isFavourite)
        coVerify(exactly = 1) { favouriteRepository.addToFavourites(film) }

        coEvery { favouriteRepository.removeFromFavourites(film.id) } returns Unit
        viewModel.toggleFavourite(film)
        advanceUntilIdle()
        assertFalse(viewModel.uiState.films[0].isFavourite)
        coVerify(exactly = 1) { favouriteRepository.removeFromFavourites(film.id) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `refreshFavourites reloads favourite ids`() = runTest {
        advanceUntilIdle()
        assertFalse(viewModel.uiState.films[0].isFavourite)

        coEvery { favouriteRepository.getAllFavourites() } returns listOf(film)
        viewModel.refreshFavourites()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.films[0].isFavourite)
    }
}