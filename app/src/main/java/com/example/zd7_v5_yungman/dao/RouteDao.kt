package com.example.zd7_v5_yungman.dao


import androidx.room.*
import com.example.zd7_v5_yungman.Route
import kotlinx.coroutines.flow.Flow

@Dao
interface RouteDao {

    @Insert
    suspend fun insert(route: Route): Long

    @Update
    suspend fun update(route: Route)

    @Delete
    suspend fun delete(route: Route)

    @Query("SELECT * FROM routes ORDER BY name ASC")
    fun getAllRoutesFlow(): Flow<List<Route>>

    @Query("SELECT * FROM routes")
    suspend fun getAllRoutes(): List<Route>
}