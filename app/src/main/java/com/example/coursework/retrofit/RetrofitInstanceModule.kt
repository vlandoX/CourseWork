package com.example.coursework.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstanceModule {

    private const val URL = "http://yakamomo.store/api/"

    private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)


    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl(URL).client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val mainApi = retrofitBuilder.create(API::class.java)

    fun getInstance(): API{
        return mainApi
    }





}