package com.example.di.presentation

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.di.App
import com.example.programmergame.GameViewModel
import com.example.programmergame.HomeActivity
import com.example.programmergame.R

class PlayGameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel
    private lateinit var buttonProgram: Button
    private lateinit var buttonWork: Button
    private lateinit var textViewMoneyRub: TextView
    private lateinit var textViewEnergy: TextView
    private lateinit var textViewStealth: TextView
    private lateinit var progressBarEnergy: ProgressBar




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_play_game, container, false)
    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        (activity?.application as App).appComponent.inject(this)
//        supportActionBar?.hide()

        viewModel.firstLoad()

        lifecycleScope.launchWhenResumed {
            viewModel.allGameValue()
                .observe(viewLifecycleOwner, {
                    textViewMoneyRub.text = it?.moneyRub?.toString()
                    textViewEnergy.text = it?.energy?.energy.toString()
                    textViewStealth.text = it?.stealth?.toString()
                    progressBarEnergy.progress = (it?.energy?.energy ?: 50)
                })
        }

        progressBarEnergy.max = 50
        progressBarEnergy.min = 0

        buttonProgram.setOnClickListener {
            viewModel.clickButtonProgram()
        }
        buttonWork.setOnClickListener {
            (activity as HomeActivity).supportFragmentManager.popBackStack()
                (activity as HomeActivity).finish()


        }
    }
    private fun initView(view: View){
        viewModel = ViewModelProvider(this)[GameViewModel::class.java]
        with(view){
            buttonProgram = findViewById(R.id.buttonProgram)
            textViewMoneyRub = findViewById(R.id.textViewMoneyRub)
            textViewEnergy = findViewById(R.id.textViewEnergy)
            textViewStealth = findViewById(R.id.textViewStealth)
            progressBarEnergy = findViewById(R.id.progressBarRating)
            buttonWork = findViewById(R.id.buttonWork)
        }}


}
