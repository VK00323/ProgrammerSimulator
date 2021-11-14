package com.example.programmergame.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import javax.inject.Inject

@TypeConverters(ValueConverter::class)
@Entity(tableName = "table_game_value")
data class GameValue @Inject constructor(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val moneyRub: Int?,
    val energy: Energy?,
    val stealth: Int?,
//    val bitcoin: Int
)