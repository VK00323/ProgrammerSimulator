package com.example.programmergame.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.programmergame.model.GameValue

@Dao
interface GameDao {

    @Query(value = "SELECT * FROM table_game_value")
    fun getAllValue(): LiveData<GameValue>

    @Query(value = "SELECT moneyRub FROM table_game_value")
    fun getMoneyValue(): LiveData<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllValue(listGameValue: GameValue)

    @Delete(entity = GameValue::class)
    fun deleteAllValue(gameValue: GameValue)

    @Query("UPDATE table_game_value SET moneyRub = moneyRub + :newSum WHERE id = 1 ")
    fun updateMoneyFirst(newSum: Int)

    @Query("SELECT * FROM table_game_value")
    fun loadAllUsers(): Array<GameValue>

    @Query("SELECT * FROM table_game_value WHERE moneyRub IS NOT NULL ")
    fun checkNull(): Array<GameValue>


//    @Update(entity = GameValue::class)
//    fun insertOneValue (int: Int){}


}