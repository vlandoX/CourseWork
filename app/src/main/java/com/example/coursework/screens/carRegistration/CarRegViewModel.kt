package com.example.coursework.screens.carRegistration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework.LoadingAlert
import com.example.coursework.retrofit.Car
import com.example.coursework.retrofit.RegistrationResponse
import com.example.coursework.retrofit.RetrofitInstanceModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CarRegViewModel(private val loadingAlert: LoadingAlert) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val responseContainer = MutableLiveData<RegistrationResponse>()

    fun signUpCar(car: Car){
        loadingAlert.startAlertDialog()
        viewModelScope.launch {
            val response = RetrofitInstanceModule.getInstance().regCar(car)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    responseContainer.postValue(response.body())
                    loadingAlert.closeAlertDialog()
                }else{
                    onError("Error : ${response.message()}")
                }

            }
        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
        loadingAlert.closeAlertDialog()
    }

}