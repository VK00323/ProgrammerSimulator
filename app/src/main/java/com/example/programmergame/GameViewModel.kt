package com.example.programmergame

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.di.App
import com.example.programmergame.database.AppDatabase
import com.example.programmergame.model.Energy
import com.example.programmergame.model.GameValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class GameViewModel(application: Application) : AndroidViewModel(application) {
    @Inject
    lateinit var db: AppDatabase


    fun allGameValue(): LiveData<GameValue> = db.gameDao().getAllValue()

    init {
        (application as App).appComponent.inject(this)
    }

    //Первая загрузка. Для того чтобы записать значения по умолчанию в базу данных
    fun firstLoad() {
        viewModelScope.launch(Dispatchers.IO) {
            val checkDB = db.gameDao().checkNull()
            if (checkDB.isEmpty()) {
                val gameValue = GameValue(1, 100, Energy(50), 50)
                db.gameDao().insertAllValue(gameValue)
            }
        }
    }


    //Кнопка программировать
    fun clickButtonProgram() {

        var randomRub: Int
        firstLoad()
        viewModelScope.launch(Dispatchers.IO) {
            randomRub = (Math.random() * (100 - 10) + 10).toInt()

            db.gameDao().clickButtonProgram(
                randomRub,
                STEALTH_FOR_CLICK_BUTTON_PROGRAM,
                ENERGY_FOR_CLICK_BUTTON_PROGRAM
            )
//            db.gameDao().updateMoneyFirst(randomRub)
            Log.d("TESTDb", randomRub.toString())
        }
    }

    companion object {
        const val STEALTH_FOR_CLICK_BUTTON_PROGRAM = 6
        const val ENERGY_FOR_CLICK_BUTTON_PROGRAM = 8
    }
}
