package com.example.di

import android.app.Application
import android.content.Context
import com.example.programmergame.database.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [AppModule::class])
class DataBaseModule {
    @Provides
    @Singleton
    fun provideDatabase(application: Application): AppDatabase {
        return AppDatabase.getInstance(application)
    }
}