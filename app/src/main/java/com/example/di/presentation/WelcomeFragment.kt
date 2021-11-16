package com.example.di.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.programmergame.R

class WelcomeFragment : Fragment() {

    private lateinit var buttonPlayGame: Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonPlayGame = view.findViewById(R.id.buttonPlayGame)


        buttonPlayGame.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.main_container,PlayGameFragment() )
                ?.addToBackStack(null)
                ?.commit()
        }



//        val intentPlayGameActivity = Intent(this, PlayGameActivity::class.java)



    }
    fun launchFragment(){

    }
}
