package com.example.studioghibli.data

import com.example.studioghibli.data.remote.GhibliApi
import com.example.studioghibli.data.remote.toDomain
import com.example.studioghibli.model.Film
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface IGhibliRepository {
    suspend fun getFilms(): List<Film>
    suspend fun getFilmById(id: String): Film
}

@Singleton
class GhibliRepository @Inject constructor(
    private val api: GhibliApi
) : IGhibliRepository {
    override suspend fun getFilms(): List<Film> = withContext(Dispatchers.IO) {
        val response = api.getFilms()
        response.data.map { dto ->
            dto.toDomain()
        }
    }

    override suspend fun getFilmById(id: String): Film = withContext(Dispatchers.IO) {
        api.getFilmById(id).data.toDomain()
    }
}