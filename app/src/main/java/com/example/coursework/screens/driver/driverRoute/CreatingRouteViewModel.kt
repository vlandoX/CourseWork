package com.example.coursework.screens.driver.driverRoute

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework.retrofit.*

import com.example.coursework.static.Static
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class CreatingRouteViewModel : ViewModel() {
    val isShowProgress = MutableLiveData<Boolean>()
    val selectedTime = MutableLiveData<Date>(Calendar.getInstance().time)
    val errorMessage = MutableLiveData<String>()
    val responseContainer = MutableLiveData<CreateTrackResponse>()



    fun createRoute(trackRequest: CreateTrackRequest){
        isShowProgress.value = true

        viewModelScope.launch {
            if(Static.user == null) return@launch
            val response = RetrofitInstanceModule.getInstance().track(trackRequest)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    responseContainer.postValue(response.body())
                    isShowProgress.value = false

                }else{
                    onError("Error : ${response.message()}")
                }

            }
        }

    }

    private fun onError(message: String) {
        errorMessage.value = message
        isShowProgress.value = false

    }
}