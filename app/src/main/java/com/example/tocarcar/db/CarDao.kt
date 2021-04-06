package com.example.tocarcar.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tocarcar.entity.Car

/**
 * Data access object
 * @author Harman, Gurvansh
 */
@Dao
interface CarDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCar(car: Car)

    @Query("SELECT * FROM car")
    fun getAllCars(): LiveData<List<Car>>
}