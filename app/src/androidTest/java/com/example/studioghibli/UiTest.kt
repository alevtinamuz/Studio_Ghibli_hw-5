package com.example.studioghibli

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.studioghibli.data.IFavouriteRepository
import com.example.studioghibli.data.IGhibliRepository
import com.example.studioghibli.model.Film
import com.example.studioghibli.ui.FilmViewModel
import com.example.studioghibli.ui.screens.FilmListScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

class FakeGhibliRepositoryWithError : IGhibliRepository {
    var shouldFail = true
    override suspend fun getFilms(): List<Film> {
        if (shouldFail) throw IOException("No internet")
        return listOf(
            Film(
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
        )
    }

    override suspend fun getFilmById(id: String): Film {
        return getFilms().first()
    }
}

class FakeFavouriteRepositoryEmpty : IFavouriteRepository {
    override suspend fun addToFavourites(film: Film) {}
    override suspend fun removeFromFavourites(filmId: String) {}
    override suspend fun getAllFavourites(): List<Film> = emptyList()
    override suspend fun getFavouriteById(filmId: String): Film? = null
}

@RunWith(AndroidJUnit4::class)
class UiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun error_thenRetry_showsData() {
        val ghibliRepository = FakeGhibliRepositoryWithError()
        val favouriteRepository = FakeFavouriteRepositoryEmpty()
        val viewModel = FilmViewModel(ghibliRepository, favouriteRepository)

        composeTestRule.setContent {
            val navController = rememberNavController()
            FilmListScreen(
                uiState = viewModel.uiState,
                onSearchQueryChange = {},
                onRetry = viewModel::retry,
                onFilmClick = {},
                onFavouriteClick = {},
                onNavigateToFavourites = {},
                onRefreshFavourites = {},
                navController = navController,
                disableAutoRefresh = true
            )
        }

        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodesWithText("Ошибка: Ошибка сети. Проверьте подключение.")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("Ошибка: Ошибка сети. Проверьте подключение.")
            .assertIsDisplayed()

        ghibliRepository.shouldFail = false

        composeTestRule.onNodeWithText("Повторить").performClick()

        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodesWithText("Howl's Moving Castle")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("Howl's Moving Castle").assertIsDisplayed()
    }
}