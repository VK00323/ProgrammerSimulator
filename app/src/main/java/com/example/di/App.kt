package com.example.di

import android.app.Application
import com.example.programmergame.BuildConfig
import com.example.programmergame.R
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.soloader.SoLoader
import com.parse.Parse
import com.parse.google.ParseGoogleUtils

class App : Application() {
    lateinit var appComponent: AppComponent


    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()

        flipperInitialization()
        parseSdkInitialization()
    }

    /**
     * Инициализация дебаггера Flipper
     */
    private fun flipperInitialization() {
        SoLoader.init(this, false)

        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
            val client = AndroidFlipperClient.getInstance(this)
//            client.addPlugin(NetworkFlipperPlugin())
            client.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))
            client.start()
        }
    }

    private fun parseSdkInitialization() {
//        Вот это заменял тоже, не повлияло, все равно успешная авторизация
//        Это я вроде добавил из проекта анвара
//        Не влияет на успешную авторизацию
//        ParseGoogleUtils.initialize("414952582173-kgk6sbucpv6hmsa2fplq98rtljl5t55o.apps.googleusercontent.com")



//        "414952582173-kgk6sbucpv6hmsa2fplq98rtljl5t55o.apps.googleusercontent.com"
//        ParseGoogleUtils.initialize(getString(R.string.back4app_app_id))

//        Падаем без инициализации парса
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id)) // if defined
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()
        )

    }


}