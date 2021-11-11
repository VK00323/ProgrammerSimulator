package com.example.programmergame.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.programmergame.model.GameValue

@Dao
interface GameDao {
    @Query(value = "SELECT * FROM table_game_value")
    fun getAllValue(): LiveData<List<GameValue>>

    @Query(value = "SELECT moneyRub FROM table_game_value")
    fun getMoneyValue(): Int


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllValue(listGameValue: GameValue)

    @Delete(entity = GameValue::class)
    fun deleteAllValue(gameValue: GameValue)

//    @Update(entity = GameValue::class)
//    fun insertOneValue (int: Int){}

    @Query("UPDATE table_game_value SET moneyRub = moneyRub + :newSum WHERE id = 1")
    fun updateMoney(newSum: Int)
}