package com.example.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private var application: Application) {

    @Provides
    @Singleton
    fun providesAppContext(): Application {
        return application
    }

}