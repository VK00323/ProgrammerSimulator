package com.example.programmergame.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.programmergame.model.GameValue

@Dao
interface GameDao {

    @Query(value = "SELECT * FROM table_game_value")
    fun getAllValue(): LiveData<GameValue>

    @Delete(entity = GameValue::class)
    fun deleteAllValue(gameValue: GameValue)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllValue(listGameValue: GameValue)

    @Query(value = "SELECT moneyRub FROM table_game_value")
    fun getMoneyValue(): LiveData<Int>

    @Query("SELECT * FROM table_game_value WHERE moneyRub IS NOT NULL ")
    fun checkNull(): Array<GameValue>

    @Query("UPDATE table_game_value SET moneyRub = moneyRub + :newSum WHERE id = 1 ")
    fun updateMoneyFirst(newSum: Int)

    @Query(value = "UPDATE table_game_value  SET moneyRub = moneyRub + :newMoney, stealth = stealth + :newStealth, energy = energy - :newEnergy WHERE id = 1  ")
    fun clickButtonProgram(newMoney: Int, newStealth: Int, newEnergy: Int)


//    @Update(entity = GameValue::class)
//    fun insertOneValue (int: Int){}


}