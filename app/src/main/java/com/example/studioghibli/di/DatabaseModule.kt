package com.example.studioghibli.di

import android.content.Context
import androidx.room.Room
import com.example.studioghibli.data.local.FavouriteFilmDao
import com.example.studioghibli.data.local.GhibliDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): GhibliDatabase {
        return Room.databaseBuilder(
            context,
            GhibliDatabase::class.java,
            "ghibli_database"
        ).build()
    }

    @Provides
    fun provideFavouriteFilmDao(database: GhibliDatabase): FavouriteFilmDao {
        return database.favouriteFilmDao()
    }
}