package com.example.zd7_v5_yungman.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.zd7_v5_yungman.DriverRouteCrossRef
@Dao
interface DriverRouteDao {
    @Insert
    suspend fun insertDriverRoute(crossRef: DriverRouteCrossRef)

    @Query("DELETE FROM driver_routes WHERE driver_id = :driverId AND route_id = :routeId")
    suspend fun deleteDriverRoute(driverId: Long, routeId: Long)

    @Query("DELETE FROM driver_routes WHERE driver_id = :driverId")
    suspend fun clearRoutesForDriver(driverId: Long)
}