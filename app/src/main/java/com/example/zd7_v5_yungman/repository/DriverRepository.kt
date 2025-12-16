package com.example.zd7_v5_yungman.repository


import androidx.lifecycle.LiveData
import com.example.zd7_v5_yungman.Driver
import com.example.zd7_v5_yungman.DriverRouteCrossRef
import com.example.zd7_v5_yungman.DriverWithRoutes
import com.example.zd7_v5_yungman.dao.DriverDao
import com.example.zd7_v5_yungman.dao.DriverRouteDao
import kotlinx.coroutines.flow.Flow
class DriverRepository(
    private val driverDao: DriverDao,
    private val driverRouteDao: DriverRouteDao
) {

    val allDriversWithRoutes: LiveData<List<DriverWithRoutes>> = driverDao.getAllDriversWithRoutesLiveData()

    suspend fun insertDriver(driver: Driver): Long = driverDao.insert(driver)

    suspend fun updateDriver(driver: Driver) = driverDao.update(driver)

    suspend fun deleteDriver(driver: Driver) = driverDao.delete(driver)

    suspend fun assignRouteToDriver(driverId: Long, routeId: Long) {
        driverRouteDao.insertDriverRoute(DriverRouteCrossRef(driverId, routeId))
    }

    suspend fun removeRouteFromDriver(driverId: Long, routeId: Long) {
        driverRouteDao.deleteDriverRoute(driverId, routeId)
    }
}