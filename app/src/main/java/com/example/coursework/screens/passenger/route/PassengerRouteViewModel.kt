package com.example.coursework.screens.passenger.route

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework.retrofit.RetrofitInstanceModule
import com.example.coursework.retrofit.Track
import com.example.coursework.static.Static
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PassengerRouteViewModel : ViewModel() {

    val trackId = MutableLiveData<String?>(null)
    val isShowProgress = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val responseContainer = MutableLiveData<Track>()

    val isReturn = MutableLiveData(false)


    fun getRoute(trackId:String){
        isShowProgress.value = true

        viewModelScope.launch {
            if(Static.user == null) return@launch

            val response = RetrofitInstanceModule.getInstance().getTrack(trackId)
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

    fun infoRoute(){
        isShowProgress.value = true

        viewModelScope.launch {
            if(Static.user == null) return@launch

            val response = RetrofitInstanceModule.getInstance().activeTrack(Static.user!!.id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    responseContainer.postValue(response.body())
                    isShowProgress.value = false
                    trackId.value = response.body()?.id

                }else{
                    onError("Error : ${response.message()}")
                }

            }
        }

    }

    fun addToRoute(){
        isShowProgress.value = true

        viewModelScope.launch {
            if(Static.user == null) return@launch
            if(trackId.value == null) return@launch

            val response =  RetrofitInstanceModule.getInstance().addPassenger(trackId.value!!)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    Static.user?.state = response.body()?.newPassengerState ?: "FREE"

                    responseContainer.postValue(response.body()?.track)
                    isReturn.value = false
                    isShowProgress.value = false
                } else {
                    onError("Error : ${response.message()}")
                }

            }


        }

    }
    fun leaveRoute(){
        isShowProgress.value = true

        viewModelScope.launch {
            if(Static.user == null) return@launch
            if(trackId.value == null) return@launch

            responseContainer.value?.let {
                val response =  RetrofitInstanceModule.getInstance().removePassenger(trackId.value!!)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {

                        Static.user?.state = response.body()?.newPassengerState ?: "FREE"

                        responseContainer.postValue(response.body()?.track)
                        isReturn.value = true
                        isShowProgress.value = false
                    }else{
                        onError("Error : ${response.message()}")
                    }

                }
            }

        }

    }



    private fun onError(message: String) {
        errorMessage.value = message
        isShowProgress.value = false

    }
}