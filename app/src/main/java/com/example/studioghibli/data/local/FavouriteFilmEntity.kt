package com.example.studioghibli.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.studioghibli.model.Film
import kotlin.String

@Entity(tableName = "favourite_film")
data class FavouriteFilmEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val originalTitle: String,
    val description: String,
    val director: String,
    val producer: String,
    val releaseDate: String,
    val runningTime: Int,
    val rtScore: String,
)

fun Film.toFavouriteEntity() : FavouriteFilmEntity = FavouriteFilmEntity(
    id = id,
    title = title,
    originalTitle = originalTitle,
    description = description,
    director = director,
    producer = producer,
    releaseDate = releaseDate,
    runningTime = runningTime,
    rtScore = rtScore,
)

fun FavouriteFilmEntity.toDomain(): Film = Film(
    id = id,
    title = title,
    originalTitle = originalTitle,
    description = description,
    director = director,
    producer = producer,
    releaseDate = releaseDate,
    runningTime = runningTime,
    rtScore = rtScore,
    isFavourite = true
)