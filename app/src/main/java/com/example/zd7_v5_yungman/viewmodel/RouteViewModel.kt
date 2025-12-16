package com.example.zd7_v5_yungman.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.zd7_v5_yungman.Route
import com.example.zd7_v5_yungman.repository.RouteRepository
import kotlinx.coroutines.launch

class RouteViewModel(
    application: Application,
    private val repository: RouteRepository
) : AndroidViewModel(application) {

    private val allRoutes = repository.allRoutes
    val allRoutesLive: LiveData<List<Route>> = allRoutes.asLiveData()

    fun insert(route: Route) = viewModelScope.launch {
        repository.insert(route)
    }

    fun update(route: Route) = viewModelScope.launch {
        repository.update(route)
    }

    fun delete(route: Route) = viewModelScope.launch {
        repository.delete(route)
    }
}