package com.example.programmergame.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.inject.Inject

@Entity(tableName = "table_game_value")
data class GameValue @Inject constructor(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val moneyRub: Int?,
//    val energy: Int,
//    val stealth:Int,
//    val bitcoin: Int
)