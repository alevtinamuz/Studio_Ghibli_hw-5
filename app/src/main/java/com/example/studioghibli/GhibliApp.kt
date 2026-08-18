package com.example.studioghibli

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.studioghibli.ui.FilmViewModel
import com.example.studioghibli.navigation.GhibliRoutes
import com.example.studioghibli.ui.FavouriteViewModel
import com.example.studioghibli.ui.FilmDetailsViewModel
import com.example.studioghibli.ui.screens.FavouriteScreen
import com.example.studioghibli.ui.screens.FilmDetailsScreen
import com.example.studioghibli.ui.screens.FilmListScreen

@Composable
fun GhibliApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = GhibliRoutes.LIST_ROUTE) {
        composable(GhibliRoutes.LIST_ROUTE) {
            val viewModel: FilmViewModel = hiltViewModel()
            val uiState = viewModel.uiState
            FilmListScreen(
                uiState = uiState,
                onSearchQueryChange = viewModel::updateSearchQuery,
                onFilmClick = { filmId ->
                    navController.navigate(GhibliRoutes.details(filmId))
                },
                onRetry = viewModel::retry,
                onFavouriteClick = viewModel::toggleFavourite,
                onNavigateToFavourites = { navController.navigate(GhibliRoutes.FAVOURITE_ROUTE) },
                onRefreshFavourites = viewModel::refreshFavourites,
                navController = navController
            )
        }
        composable(
            route = GhibliRoutes.DETAILS_ROUTE_PATTERN,
            arguments = listOf(
                navArgument(GhibliRoutes.FILM_ID_ARG
                ) {
                    type = NavType.StringType
                }
            )
        ) { _ ->
            val detailsViewModel: FilmDetailsViewModel = hiltViewModel()
            val detailsUiState = detailsViewModel.uiState
            FilmDetailsScreen(
                uiState = detailsUiState,
                onBack = { navController.popBackStack() },
                onRetry = detailsViewModel::retry
            )
        }
        composable(GhibliRoutes.FAVOURITE_ROUTE) {
            val viewModel: FavouriteViewModel = hiltViewModel()
            val uiState = viewModel.uiState
            FavouriteScreen(
                uiState = uiState,
                onFilmClick = { filmId ->
                    navController.navigate(GhibliRoutes.details(filmId))
                },
                onRetry = viewModel::retry,
                onToggleFavourite = viewModel::toggleFavourite,
                onBack = { navController.popBackStack() }
            )
        }
    }
}