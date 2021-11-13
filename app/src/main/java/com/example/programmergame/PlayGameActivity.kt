package com.example.programmergame

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.di.App
import com.example.programmergame.database.AppDatabase
import javax.inject.Inject

class PlayGameActivity : AppCompatActivity() {
    lateinit var buttonProgram: Button
    lateinit var viewModel: GameViewModel
    lateinit var textViewMoneyRub: TextView

    @Inject
    lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as App).appComponent.inject(this)
        supportActionBar?.hide()
        setContentView(R.layout.activity_play_game)
        buttonProgram = findViewById(R.id.buttonProgram)
        textViewMoneyRub = findViewById(R.id.textViewMoneyRub)
        viewModel = ViewModelProvider(this)[GameViewModel::class.java]

        lifecycleScope.launchWhenResumed {
            viewModel.allGameValue()
                .observe(this@PlayGameActivity, {
                    textViewMoneyRub.text = it?.toString() ?: "100"
                })
        }

        buttonProgram.setOnClickListener {
            viewModel.download()
        }
    }
}
//        viewModel.allGameValue().observe(this, { gameValue ->
//            buttonProgram.setOnClickListener {
//                viewModel.download()
//                textViewMoneyRub.text = gameValue.moneyRub?.toString()
//                Log.d("TESTButton", viewModel.allGameValue().value.toString())
//            }
//        })

//    suspend fun initView() {
//        coroutineScope {
//            launch {
//
//            }
//        }
//    }
//
//        val moneyObserver =  Observer<List<GameValue>> {newMoney->
//            textViewMoneyRub.text = newMoney.map { it.moneyRub }.toString()
//            Log.d("TESTMoneyObserver", newMoney.toString())
//        }
//        viewModel.allGameValue().observe(this, moneyObserver)
//        buttonProgram.setOnClickListener {
//            viewModel.download()
//            Log.d("TESTButton", viewModel.allGameValue().value.toString())
//        }
//        db.gameDao().getAllValue().observe(this, {
//
//            Log.d("TEST ", it.toString())
//        }
//        )
