package com.example.zd7_v5_yungman.factory


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.zd7_v5_yungman.repository.BusRepository
import com.example.zd7_v5_yungman.viewmodel.BusViewModel

class BusViewModelFactory(
    private val application: Application,
    private val repository: BusRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BusViewModel::class.java)) {
            return BusViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}