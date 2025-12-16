package com.example.zd7_v5_yungman.factory


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.zd7_v5_yungman.repository.RouteRepository
import com.example.zd7_v5_yungman.viewmodel.RouteViewModel

class RouteViewModelFactory(
    private val application: Application,
    private val repository: RouteRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RouteViewModel::class.java)) {
            return RouteViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}