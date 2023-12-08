package com.example.coursework.retrofit

import java.util.Date

data class SignUpRequest(
    val username: String,
    val phoneNumber: String,
    val password: String,
    val role: String
)

data class AuthRequest(
    val phoneNumber: String,
    val password: String
)

data class CarRequest(
    val model: String,
    val color: String,
    val regNumber: String
)

data class TrackRequest(
    val maxSeats: Int,
    //val departureTime: Date,
    val driverComment: String
)
