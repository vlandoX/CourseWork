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

data class CreateTrackRequest(
    val startLocation: Location,
    val endLocation: Location,
    val maxSeats: Int,
    val departureTime: String,
    val driverComment: String
)






