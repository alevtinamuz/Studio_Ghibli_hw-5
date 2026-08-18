package com.example.studioghibli.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavouriteFilmEntity::class], version = 1, exportSchema = false)
abstract class GhibliDatabase: RoomDatabase() {
    abstract fun favouriteFilmDao(): FavouriteFilmDao
}