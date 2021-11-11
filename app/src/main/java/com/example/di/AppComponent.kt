package com.example.di

import com.example.programmergame.GameViewModel
import com.example.programmergame.HomeActivity
import com.example.programmergame.PlayGameActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataBaseModule::class])
interface AppComponent {

    fun inject (playGameActivity: PlayGameActivity)
    fun inject (homeActivity: HomeActivity)
    fun inject (viewModel: GameViewModel)

}