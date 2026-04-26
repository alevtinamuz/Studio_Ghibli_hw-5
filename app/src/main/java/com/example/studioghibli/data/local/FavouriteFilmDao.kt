package com.example.studioghibli.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavouriteFilmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavourites(film: FavouriteFilmEntity)

    @Query("DELETE FROM favourite_film WHERE id = :filmId")
    suspend fun removeFromFavourites(filmId: String)

    @Query("SELECT * FROM favourite_film ORDER BY title")
    suspend fun getAllFavourites(): List<FavouriteFilmEntity>

    @Query("SELECT * FROM favourite_film WHERE id = :filmId")
    suspend fun getFavouriteById(filmId: String): FavouriteFilmEntity?
}