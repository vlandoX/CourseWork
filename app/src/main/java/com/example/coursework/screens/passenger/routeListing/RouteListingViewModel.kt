package com.example.coursework.screens.passenger.routeListing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework.retrofit.RetrofitInstanceModule
import com.example.coursework.retrofit.TrackListingPage
import kotlinx.coroutines.*

class RouteListingViewModel : ViewModel() {
    val responseContainer = MutableLiveData<TrackListingPage>()
    val errorMessage = MutableLiveData<String>()
    val isShowProgress = MutableLiveData<Boolean>()
    val expressionToSearch = MutableLiveData("")

    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled : ${throwable.localizedMessage}")
    }

    fun getRoutePageFromAPI(sX: String,sY:String,eX:String,eY:String,ipp:Int = 50, page:Int = 0) {

        val _sX:Double = sX.toDoubleOrNull() ?: 0.0
        val _sY:Double = sY.toDoubleOrNull() ?: 0.0
        val _eX:Double = eX.toDoubleOrNull() ?: 0.0
        val _eY:Double = eY.toDoubleOrNull() ?: 0.0

        isShowProgress.value = true
        job = viewModelScope.launch {
            val response = RetrofitInstanceModule.getInstance().tracksPage(ipp,page,_sX,_sY,_eX,_eY)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    responseContainer.postValue(response.body())
                    isShowProgress.value = false
                } else {
                    onError("Error : ${response.message()}")
                }
            }
        }


    }

    private fun onError(message: String) {
        errorMessage.value = message
        isShowProgress.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}