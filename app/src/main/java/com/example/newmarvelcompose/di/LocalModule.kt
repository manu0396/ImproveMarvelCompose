package com.example.newmarvelcompose.di

import android.content.Context
import com.example.newmarvelcompose.data.local.LocalDatabase
import com.example.newmarvelcompose.data.local.MarvelDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule() {

    @Provides
    @Singleton
    fun provideDB(@ApplicationContext context: Context): LocalDatabase {
        return LocalDatabase.getInstance(context = context)
    }

    @Provides
    @Singleton
    fun provideDao(localDatabase: LocalDatabase): MarvelDAO {
        return localDatabase.dao()
    }
}