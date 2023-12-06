package com.example.coursework.screens.signIn

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework.LoadingAlert
import com.example.coursework.retrofit.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignInViewModel(private val loadingAlert: LoadingAlert) : ViewModel() {
    val token = MutableLiveData<String>()
    val isShowProgress = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val responseContainer = MutableLiveData<RegistrationResponse>()


    fun authUser(authRequest: AuthRequest){
        //isShowProgress.value = true
        loadingAlert.startAlertDialog()
        viewModelScope.launch {
            val response = RetrofitInstanceModule.getInstance().auth(authRequest)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    responseContainer.postValue(response.body())
                    //isShowProgress.value = false
                    loadingAlert.closeAlertDialog()
                }else{
                    onError("Error : ${response.message()}")
                }

            }
        }

    }



    private fun onError(message: String) {
        errorMessage.value = message
        //isShowProgress.value = false
        loadingAlert.closeAlertDialog()
    }
}