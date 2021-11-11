package com.example.programmergame

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.di.App
import com.example.programmergame.database.AppDatabase
import com.example.programmergame.model.GameValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class GameViewModel(application: Application) : AndroidViewModel(application) {
    @Inject
    lateinit var db: AppDatabase
    private var randomCount :List<GameValue> = arrayListOf()
    fun allGameValue() = db.gameDao().getAllValue()



    init {
        (application as App).appComponent.inject(this)

    }


    fun download() {
        viewModelScope.launch(Dispatchers.IO) {
            val gameValue = GameValue(moneyRub = (Math.random() * (100 - 10) + 10).toInt())
            val randomRub = (Math.random() * (100 - 10) + 10).toInt()
//            db.gameDao().insertAllValue(gameValue)
            db.gameDao().updateMoney(randomRub)
            Log.d("TESTDownload", randomCount.toString())
            Log.d("TESTDownloadAllGame", allGameValue().toString())
            val dbMoney = db.gameDao().getMoneyValue()
            Log.d("TESTDb", dbMoney.toString())

        }


    }


}
