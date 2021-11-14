package com.example.programmergame

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var buttonPlayGame: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonPlayGame = findViewById(R.id.buttonPlayGame)
        val intentPlayGameActivity = Intent(this, PlayGameActivity::class.java)



        buttonPlayGame.setOnClickListener {
            startActivity(intentPlayGameActivity)


        }
    }
}