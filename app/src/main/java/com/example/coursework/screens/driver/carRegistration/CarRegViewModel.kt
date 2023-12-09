package com.example.coursework.screens.driver.carRegistration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework.LoadingAlert
import com.example.coursework.retrofit.Car
import com.example.coursework.retrofit.RegistrationResponse
import com.example.coursework.retrofit.RetrofitInstanceModule
import com.example.coursework.retrofit.User
import com.example.coursework.static.Static
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CarRegViewModel(private val loadingAlert: LoadingAlert) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val responseContainer = MutableLiveData<User>()

    fun signUpCar(car: Car){
        loadingAlert.startAlertDialog()
        viewModelScope.launch {
            if(Static.user == null) return@launch
            val response = RetrofitInstanceModule.getInstance().regCar(Static.user!!.id,car)
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