package com.example.studioghibli.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface GhibliApi {
    @GET("films")
    suspend fun getFilms () : GhibliListResponse<GhibliDto>

    @GET("films/{id}")
    suspend fun getFilmById(@Path("id") id: String): GhibliItemResponse<GhibliDto>
}