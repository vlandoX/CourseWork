package com.example.coursework.screens.carRegistration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coursework.LoadingAlert

class CarRegViewModelFactory (private val loadingAlert: LoadingAlert): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarRegViewModel::class.java)){
            return CarRegViewModel(loadingAlert) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}