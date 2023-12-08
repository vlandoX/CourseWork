package com.example.coursework.retrofit

import retrofit2.Response
import retrofit2.http.*

interface API {

    @POST("users")
    suspend fun registration(
        @Body signUpRequest: SignUpRequest
    ): Response<RegistrationResponse>

    @POST("users")
    suspend fun regCar(
        @Body car: Car
    ): Response<RegistrationResponse>

    @POST("auth/login")
    suspend fun auth(
        @Body authRequest: AuthRequest
    ): Response<RegistrationResponse>

    @POST("tracks")
    suspend fun track(
        @Body trackRequest: TrackRequest
    ): Response<RegistrationResponse>
}
