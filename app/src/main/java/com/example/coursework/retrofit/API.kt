package com.example.coursework.retrofit

import retrofit2.Response
import retrofit2.http.*

interface API {

    @POST("users")
    suspend fun registration(
        @Body signUpRequest: SignUpRequest
    ): Response<RegistrationResponse>


    @POST("auth/login")
    suspend fun auth(
        @Body authRequest: AuthRequest
    ): Response<RegistrationResponse>
}