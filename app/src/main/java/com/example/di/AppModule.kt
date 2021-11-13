package com.example.di

import android.app.Application
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
//    @Provides
//    @Singleton
//    fun provideGameValue (id: Int,money:Int): GameValue{
//        return GameValue(id,money
//        )
//    }

}