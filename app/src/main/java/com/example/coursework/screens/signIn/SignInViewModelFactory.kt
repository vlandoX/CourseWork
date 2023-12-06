package com.example.coursework.screens.signIn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coursework.LoadingAlert

class SignInViewModelFactory(private val loadingAlert: LoadingAlert): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)){
            return SignInViewModel(loadingAlert) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}