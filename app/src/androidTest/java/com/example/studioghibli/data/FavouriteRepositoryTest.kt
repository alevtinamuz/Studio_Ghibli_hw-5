package com.example.studioghibli.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.studioghibli.data.local.GhibliDatabase
import com.example.studioghibli.model.Film
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavouriteRepositoryTest {
    private lateinit var database: GhibliDatabase
    private lateinit var repository: FavouriteRepository

    private val film = Film(
        "1",
        "Howl's Moving Castle",
        "ハウルの動く城",
        "The best",
        "Hayao Miyazaki",
        "Toshio Suzuki",
        "2004",
        119,
        "87"
    )

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            GhibliDatabase::class.java
        ).allowMainThreadQueries().build()
        repository = FavouriteRepository(database.favouriteFilmDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun addToFavourites_returns_saved_film() = runTest {
        repository.addToFavourites(film)
        val favourites = repository.getAllFavourites()
        assertEquals(1, favourites.size)
        assertEquals(film.id, favourites[0].id)
    }

    @Test
    fun removeFromFavourites_removes_film() = runTest {
        repository.addToFavourites(film)
        repository.removeFromFavourites(film.id)
        val favourites = repository.getAllFavourites()
        assertTrue(favourites.isEmpty())
    }

    @Test
    fun addToFavourites_twice() = runTest {
        repository.addToFavourites(film)
        repository.addToFavourites(film)
        val favourites = repository.getAllFavourites()
        assertEquals(1, favourites.size)
    }
}