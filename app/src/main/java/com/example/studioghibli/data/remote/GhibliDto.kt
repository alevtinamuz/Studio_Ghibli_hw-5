package com.example.studioghibli.data.remote

import com.example.studioghibli.model.Film
import com.google.gson.annotations.SerializedName

data class GhibliListResponse<T>(
    val data: List<T>
)
data class GhibliItemResponse<T>(
    val data: T
)

data class GhibliDto (
    val id: String,
    val title: String,
    @SerializedName("original_title")
    val originalTitle: String,
    val description: String,
    val director: String,
    val producer: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("running_time")
    val runningTime: String,
    @SerializedName("rt_score")
    val rtScore: String,
)

fun GhibliDto.toDomain(): Film = Film(
    id = id,
    title = title,
    originalTitle = originalTitle,
    description = description,
    director = director,
    producer = producer,
    releaseDate = releaseDate,
    runningTime = runningTime.toIntOrNull() ?: 0,
    rtScore = rtScore,
)