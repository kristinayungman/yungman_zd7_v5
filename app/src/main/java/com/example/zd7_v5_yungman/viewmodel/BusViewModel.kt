package com.example.zd7_v5_yungman.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.zd7_v5_yungman.Bus
import com.example.zd7_v5_yungman.repository.BusRepository
import kotlinx.coroutines.launch

class BusViewModel(
    application: Application,
    private val repository: BusRepository
) : AndroidViewModel(application) {

    private val allBuses = repository.allBuses

    val allBusesLive: LiveData<List<Bus>> = allBuses.asLiveData()

    fun insert(bus: Bus) = viewModelScope.launch {
        repository.insert(bus)
    }

    fun update(bus: Bus) = viewModelScope.launch {
        repository.update(bus)
    }

    fun delete(bus: Bus) = viewModelScope.launch {
        repository.delete(bus)
    }
    fun getGroupedBuses(callback: (Map<String, List<Bus>>) -> Unit) = viewModelScope.launch {
        val grouped = repository.getBusesGroupedByCondition()
        callback(grouped)
    }
}