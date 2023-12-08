package com.example.coursework.retrofit

data class RegistrationResponse(
    val token: String,
    val user: User
)

data class User(
    val id: String,
    val username: String,
    val phoneNumber: String,
    val role: String,
    var car: Car? = null
)

data class Car(
    val model: String,
    val color: String,
    val regNumber: String
)

