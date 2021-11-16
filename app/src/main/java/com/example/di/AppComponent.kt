package com.example.di

import com.example.di.presentation.PlayGameFragment
import com.example.programmergame.GameViewModel
import com.example.programmergame.HomeActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataBaseModule::class])
interface AppComponent {

    fun inject(homeActivity: HomeActivity)
    fun inject(viewModel: GameViewModel)
    fun inject(fragment: PlayGameFragment)


}