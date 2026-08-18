package com.example.studioghibli.di

import com.example.studioghibli.data.FavouriteRepository
import com.example.studioghibli.data.GhibliRepository
import com.example.studioghibli.data.IFavouriteRepository
import com.example.studioghibli.data.IGhibliRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindGhibliRepository(impl: GhibliRepository): IGhibliRepository

    @Binds
    abstract fun bindFavouriteRepository(impl: FavouriteRepository): IFavouriteRepository
}