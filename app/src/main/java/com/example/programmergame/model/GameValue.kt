package com.example.programmergame.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_game_value")
data class GameValue (
    @PrimaryKey
    val id:Int =1,
    val moneyRub:Int,
//    val energy:Int,
//    val stealth:Int,
//    val bitcoin: Int
)