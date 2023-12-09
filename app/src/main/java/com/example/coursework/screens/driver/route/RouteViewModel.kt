package com.example.coursework.screens.driver.route

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework.retrofit.CreateTrackRequest
import com.example.coursework.retrofit.RetrofitInstanceModule
import com.example.coursework.retrofit.Track
import com.example.coursework.static.Static
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RouteViewModel : ViewModel() {
    val isShowProgress = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val responseContainer = MutableLiveData<Track>()

    val isReturn = MutableLiveData(false)


    fun infoRoute(){
        isShowProgress.value = true

        viewModelScope.launch {
            if(Static.user == null) return@launch

            val response = RetrofitInstanceModule.getInstance().activeTrack(Static.user!!.id)
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

    fun deleteRoute(){
        isShowProgress.value = true

        viewModelScope.launch {
            if(Static.user == null) return@launch

            responseContainer.value?.let {
                val response =  RetrofitInstanceModule.getInstance().deleteTrack(it.id)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {

                        Static.user?.state = response.body()?.newDriverState ?: "FREE"

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