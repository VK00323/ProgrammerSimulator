package com.example.programmergame

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.di.App

class PlayGameActivity : AppCompatActivity() {

    private lateinit var viewModel: GameViewModel
    private lateinit var buttonProgram: Button
    private lateinit var textViewMoneyRub: TextView
    private lateinit var textViewEnergy: TextView
    private lateinit var textViewStealth: TextView
    private lateinit var progressBarEnergy: ProgressBar

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as App).appComponent.inject(this)
        supportActionBar?.hide()
        setContentView(R.layout.activity_play_game)
        buttonProgram = findViewById(R.id.buttonProgram)
        textViewMoneyRub = findViewById(R.id.textViewMoneyRub)
        textViewEnergy = findViewById(R.id.textViewEnergy)
        textViewStealth = findViewById(R.id.textViewStealth)
        progressBarEnergy = findViewById(R.id.progressBarRating)
        viewModel = ViewModelProvider(this)[GameViewModel::class.java]
        viewModel.firstLoad()

        lifecycleScope.launchWhenResumed {
            viewModel.allGameValue()
                .observe(this@PlayGameActivity, {
                    textViewMoneyRub.text = it?.moneyRub?.toString()
                    textViewEnergy.text = it?.energy?.energy.toString()
                    textViewStealth.text = it?.stealth?.toString()
                    progressBarEnergy.progress = (it?.energy?.energy?: 50)
                })
        }

        progressBarEnergy.max = 50
        progressBarEnergy.min = 0

        buttonProgram.setOnClickListener {
            viewModel.clickButtonProgram()
        }
    }
}