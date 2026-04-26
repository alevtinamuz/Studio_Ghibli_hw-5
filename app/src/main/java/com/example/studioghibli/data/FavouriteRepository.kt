package com.example.studioghibli.data

import com.example.studioghibli.data.local.FavouriteFilmDao
import com.example.studioghibli.data.local.toDomain
import com.example.studioghibli.data.local.toFavouriteEntity
import com.example.studioghibli.model.Film
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface IFavouriteRepository {
    suspend fun addToFavourites(film: Film)
    suspend fun removeFromFavourites(filmId: String)
    suspend fun getAllFavourites(): List<Film>
    suspend fun getFavouriteById(filmId: String): Film?
}

class FavouriteRepository @Inject constructor(
    private val favouriteFilmDao: FavouriteFilmDao
) : IFavouriteRepository {
    override suspend fun addToFavourites(film: Film) {
        favouriteFilmDao.addToFavourites(film.toFavouriteEntity())
    }

    override suspend fun removeFromFavourites(filmId: String) {
        favouriteFilmDao.removeFromFavourites(filmId)
    }
    override suspend fun getAllFavourites(): List<Film> = withContext(Dispatchers.IO) {
        favouriteFilmDao.getAllFavourites().map { it.toDomain() }
    }

    override suspend fun getFavouriteById(filmId: String): Film? = withContext(Dispatchers.IO) {
        favouriteFilmDao.getFavouriteById(filmId)?.toDomain()
    }
}