package com.example.coursework.retrofit

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
