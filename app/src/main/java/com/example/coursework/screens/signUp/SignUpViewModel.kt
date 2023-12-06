package com.example.coursework.screens.signUp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework.LoadingAlert
import com.example.coursework.retrofit.RegistrationResponse
import com.example.coursework.retrofit.RetrofitInstanceModule
import com.example.coursework.retrofit.SignUpRequest
import com.example.coursework.retrofit.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpViewModel(private val loadingAlert: LoadingAlert) : ViewModel() {

    val isShowProgress = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val responseContainer = MutableLiveData<RegistrationResponse>()



    fun createUser(signUpRequest: SignUpRequest){
        //isShowProgress.value = true
        loadingAlert.startAlertDialog()
        viewModelScope.launch {
            val response = RetrofitInstanceModule.getInstance().registration(signUpRequest)
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


    fun validName(nameText: String): String? {

        if (nameText.isNullOrBlank()) {
            return "Имя не должно быть пустым"
        }
        if (!nameText.matches(".*[a-zA-Z].*".toRegex())) {
            return "Имя должно содержать только символы"
        }
        return null
    }

    fun validPhone(phoneText: String): String? {

        if (phoneText.isNullOrBlank()) {
            return "Номер не должен быть пустым"
        }
        if (!phoneText.matches(".*[0-9].*".toRegex())) {
            return "Номер телефона должен содержать толко цифры"
        }
        if (phoneText.length != 10) {
            return "Номер должен состоять из 10 цифр"
        }
        return null
    }

    fun validPassword(passwordText: String): String? {

        if (passwordText.isNullOrBlank()) {
            return "Пароль не должен быть пустым"
        }
        if (passwordText.length != 8) {
            return "Пароль должен содержать 8 символов"
        }
        if (!passwordText.matches(".*[0-9].*".toRegex())) {
            return "Пароль должен содержать хотябы одну цифру"
        }
        if (!passwordText.matches(".*[@#\$%^&+=].*".toRegex())) {
            return "Пароль должен содержать хотябы один символ (@#\$%^&+=)"
        }

        return null
    }

    fun validRepeatPassword(passwordRepeatText: String, passwordText: String): String?{
        if (passwordRepeatText.isNullOrBlank()) {
            return "Необходимо повторить пароль"
        }
        if(passwordText != passwordRepeatText){
            return "Отличается от созданного пароля"
        }
        return null
    }




}