package com.example.zd7_v5_yungman.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.zd7_v5_yungman.*

@Dao
interface DriverDao {
    @Insert
    suspend fun insert(driver: Driver): Long

    @Update
    suspend fun update(driver: Driver)

    @Delete
    suspend fun delete(driver: Driver)

    @Query("DELETE FROM driver_routes WHERE driver_id = :driverId")
    suspend fun clearRoutesForDriver(driverId: Long)

    @Insert
    suspend fun insertDriverRoute(crossRef: DriverRouteCrossRef)

    @Query("SELECT * FROM drivers ORDER BY name")
    fun getAllDriversWithRoutesLiveData(): LiveData<List<DriverWithRoutes>>

    // ✅ Сделали suspend
    @Query("SELECT * FROM buses ORDER BY id")
    suspend fun getAllBuses(): List<Bus>

    @Query("SELECT * FROM routes ORDER BY name")
    suspend fun getAllRoutes(): List<Route>
}