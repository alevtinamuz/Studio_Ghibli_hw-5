package com.example.studioghibli.navigation

object GhibliRoutes {
    const val LIST_ROUTE = "films"
    const val DETAILS_ROUTE = "details"
    const val FAVOURITE_ROUTE = "favourites"
    const val FILM_ID_ARG = "filmId"
    const val DETAILS_ROUTE_PATTERN = "$DETAILS_ROUTE/{$FILM_ID_ARG}"
    fun details(filmId: String): String = "$DETAILS_ROUTE/$filmId"
}