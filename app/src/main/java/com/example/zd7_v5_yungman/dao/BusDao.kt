package com.example.zd7_v5_yungman.dao


import androidx.room.*
import com.example.zd7_v5_yungman.Bus
import kotlinx.coroutines.flow.Flow

@Dao
interface BusDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bus: Bus): Long

    @Update
    suspend fun update(bus: Bus)

    @Delete
    suspend fun delete(bus: Bus)

    @Query("SELECT * FROM buses ORDER BY id ASC")
    fun getAllBusesFlow(): Flow<List<Bus>>

    @Query("SELECT * FROM buses")
    suspend fun getAllBuses(): List<Bus> // ← ДОБАВЬТЕ ЭТУ СТРОКУ

    @Query("SELECT * FROM buses WHERE condition = :condition")
    suspend fun getBusesByCondition(condition: String): List<Bus>

    @Query("SELECT DISTINCT condition FROM buses")
    suspend fun getAllUniqueConditions(): List<String>

    @Query("SELECT * FROM buses WHERE depreciation_percentage >= :min AND depreciation_percentage < :max")
    suspend fun getBusesByDepreciationRange(min: Float, max: Float): List<Bus>

    @Query("SELECT * FROM buses WHERE depreciation_percentage >= :min AND depreciation_percentage < :max")
    fun getBusesByDepreciationRangeFlow(min: Float, max: Float): Flow<List<Bus>>
}