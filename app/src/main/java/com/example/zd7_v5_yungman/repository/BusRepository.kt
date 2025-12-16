package com.example.zd7_v5_yungman.repository


import com.example.zd7_v5_yungman.Bus
import com.example.zd7_v5_yungman.dao.BusDao
import kotlinx.coroutines.flow.Flow

class BusRepository(private val busDao: BusDao) {

    val allBuses: Flow<List<Bus>> = busDao.getAllBusesFlow()

    suspend fun insert(bus: Bus): Long = busDao.insert(bus)

    suspend fun update(bus: Bus) = busDao.update(bus)

    suspend fun delete(bus: Bus) = busDao.delete(bus)

    suspend fun getAllUniqueConditions(): List<String> = busDao.getAllUniqueConditions()

    suspend fun getBusesByCondition(condition: String): List<Bus> {
        return busDao.getBusesByCondition(condition)
    }
    suspend fun getBusesGroupedByCondition(): Map<String, List<Bus>> {
        val allBuses = busDao.getAllBuses() // убедитесь, что этот метод есть в DAO
        return allBuses.groupBy { it.condition }
    }

}