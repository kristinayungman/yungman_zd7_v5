package com.example.zd7_v5_yungman.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.zd7_v5_yungman.AppDatabase
import com.example.zd7_v5_yungman.Bus
import com.example.zd7_v5_yungman.Driver
import com.example.zd7_v5_yungman.DriverWithRoutes
import com.example.zd7_v5_yungman.repository.BusRepository
import com.example.zd7_v5_yungman.repository.DriverRepository
import kotlinx.coroutines.launch

class DriverViewModel(
    application: Application,
    private val driverRepository: DriverRepository,
    private val busRepository: BusRepository
) : AndroidViewModel(application) {

    // Водители с маршрутами
    private val allDrivers = driverRepository.allDriversWithRoutes

    private val driverDao = AppDatabase.getDatabase(application).driverDao()

    // ✅ Просто используем LiveData напрямую
    val allDriversLive: LiveData<List<DriverWithRoutes>> = driverDao.getAllDriversWithRoutesLiveData()

    // Все автобусы (нужны при редактировании водителя)
    private val allBuses = busRepository.allBuses
    val allBusesLive: LiveData<List<Bus>> = allBuses.asLiveData()

    fun insertDriver(driver: Driver) = viewModelScope.launch {
        driverRepository.insertDriver(driver)
    }

    fun updateDriver(driver: Driver) = viewModelScope.launch {
        driverRepository.updateDriver(driver)
    }

    fun deleteDriver(driver: Driver) = viewModelScope.launch {
        driverRepository.deleteDriver(driver)
    }

    fun assignRouteToDriver(driverId: Long, routeId: Long) = viewModelScope.launch {
        driverRepository.assignRouteToDriver(driverId, routeId)
    }

    fun removeRouteFromDriver(driverId: Long, routeId: Long) = viewModelScope.launch {
        driverRepository.removeRouteFromDriver(driverId, routeId)
    }
}