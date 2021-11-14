package com.example.programmergame.model

import androidx.room.TypeConverter
//Установить ограничение у показателя энергия( нельзя опуститься ниже нуля)
class ValueConverter {
    @TypeConverter
    fun fromTimestamp(energy: Int?): Energy? {
        if (energy != null) {
            if (energy <= 0) {
                return Energy(0)
            }
        }
        return energy?.let { Energy(it) }
    }

    @TypeConverter
    fun dateToTimestamp(energy: Energy?): Int? {
        if (energy != null) {
            return energy.energy
        }
        return null
    }
}