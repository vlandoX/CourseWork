package com.example.coursework.screens.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coursework.LoadingAlert
import com.example.coursework.screens.signIn.SignInViewModel

class SignUpViewModelFactory (private val loadingAlert: LoadingAlert): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)){
            return SignUpViewModel(loadingAlert) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}