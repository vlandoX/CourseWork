package com.example.coursework.screens.driverRoute

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework.retrofit.RegistrationResponse
import com.example.coursework.retrofit.RetrofitInstanceModule
import com.example.coursework.retrofit.TrackRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreatingRouteViewModel : ViewModel() {
    val isShowProgress = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val responseContainer = MutableLiveData<RegistrationResponse>()



    fun createRoute(trackRequest: TrackRequest){
        isShowProgress.value = true

        viewModelScope.launch {
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