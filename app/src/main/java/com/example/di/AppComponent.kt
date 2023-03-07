package com.example.di

import com.example.programmergame.GameViewModel
import com.example.programmergame.MainActivity
import com.example.programmergame.StatisticFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataBaseModule::class])
interface AppComponent {

    fun inject(playGameActivity: StatisticFragment)
    fun inject(homeActivity: MainActivity)
    fun inject(viewModel: GameViewModel)

}