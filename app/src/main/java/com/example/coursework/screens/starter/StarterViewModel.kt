package com.example.coursework.screens.starter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework.retrofit.AuthRequest
import com.example.coursework.retrofit.RegistrationResponse
import com.example.coursework.retrofit.RetrofitInstanceModule
import com.example.coursework.retrofit.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StarterViewModel : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val responseContainer = MutableLiveData<User>()


    fun getUser(userId:String){
        viewModelScope.launch {
            val response = RetrofitInstanceModule.getInstance().getUser(userId)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    responseContainer.postValue(response.body())
                }else{
                    onError("Error : ${response.message()}")
                }
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
    }

}