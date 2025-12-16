package com.example.zd7_v5_yungman.factory


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.zd7_v5_yungman.repository.BusRepository
import com.example.zd7_v5_yungman.repository.DriverRepository
import com.example.zd7_v5_yungman.viewmodel.DriverViewModel

class DriverViewModelFactory(
    private val application: Application,
    private val driverRepository: DriverRepository,
    private val busRepository: BusRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DriverViewModel::class.java)) {
            return DriverViewModel(application, driverRepository, busRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}