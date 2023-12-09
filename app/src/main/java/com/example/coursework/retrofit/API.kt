package com.example.coursework.retrofit

import retrofit2.Response
import retrofit2.http.*

interface API {

    @POST("users")
    suspend fun registration(
        @Body signUpRequest: SignUpRequest
    ): Response<RegistrationResponse>

    @GET("users/{userId}")
    suspend fun getUser(
        @Path("userId") userId: String
    ): Response<User>

    @POST("users/{userId}/car")
    suspend fun regCar(
        @Path("userId") userId: String,
        @Body car: Car
    ): Response<User>

    @POST("auth/login")
    suspend fun auth(
        @Body authRequest: AuthRequest
    ): Response<RegistrationResponse>


    @POST("tracks")
    suspend fun track(
        @Body trackRequest: CreateTrackRequest
    ): Response<CreateTrackResponse>


    @GET("users/{userId}/active_track")
    suspend fun activeTrack(
        @Path("userId") userId: String,
    ):Response<Track>

    @GET("tracks/{trackId}")
    suspend fun getTrack(
        @Path("trackId") trackId: String,
    ) :Response<Track>

    @DELETE("tracks/{trackId}")
    suspend fun deleteTrack(
        @Path("trackId") trackId: String,
    ) :Response<DeleteTrackResponse>

    @GET("tracks")
    suspend fun tracksPage(
        @Query("ipp") ipp: Int,
        @Query("page") page: Int,
        @Query("sX") sX: Double,
        @Query("sY") sY: Double,
        @Query("eX") eX: Double,
        @Query("eY") eY: Double,
    ):Response<TrackListingPage>

    @POST("tracks/{trackId}/add_passenger")
    suspend fun addPassenger(
        @Path("trackId") trackId: String,
    ):Response<CreateTrackResponse>

    @POST("tracks/{trackId}/remove_passenger")
    suspend fun removePassenger(
        @Path("trackId") trackId: String,
    ):Response<CreateTrackResponse>



}
