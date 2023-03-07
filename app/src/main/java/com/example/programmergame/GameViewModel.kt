package com.example.programmergame

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
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

    fun allGameValue(): LiveData<Int> = db.gameDao().getMoneyValue()

    init {
        (application as App).appComponent.inject(this)
    }

    private fun firstLoad() {
        val checkDB = db.gameDao().checkNull()
        if (checkDB.isEmpty()) {
            val gameValue = GameValue(1, 100)
            db.gameDao().insertAllValue(gameValue)
        }
    }

    /**
     *     Увелечение значения Денег
     */
    fun download() {
        var randomRub: Int
        viewModelScope.launch(Dispatchers.IO) {
            firstLoad()
            randomRub = (Math.random() * (100 - 10) + 10).toInt()
            db.gameDao().updateMoneyFirst(randomRub)
            Log.d("TESTDb", randomRub.toString())
        }
    }
}
