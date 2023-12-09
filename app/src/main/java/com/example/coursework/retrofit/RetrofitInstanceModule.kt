package com.example.coursework.retrofit

import com.example.coursework.static.Static
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstanceModule {

    private const val URL = "http://yakamomo.store/api/"

    private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)


    private val client = OkHttpClient.Builder()
        .addInterceptor(HeaderInterceptor())
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

    class HeaderInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val originalRequest = chain.request()

            val auth = Static.token ?: ""

            val requestBuilder = originalRequest.newBuilder()
                .header("x-api-key", "80e19f7a-95bc-11ee-b9d1-0242ac120002")
                .addHeader("Authorization", "Bearer $auth")
                // Add other headers if needed
                .method(originalRequest.method, originalRequest.body)

            val request = requestBuilder.build()
            return chain.proceed(request)
        }
    }





}