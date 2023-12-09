package com.example.coursework.retrofit

import java.util.*
import java.io.Serializable
import kotlin.collections.ArrayList

data class RegistrationResponse(
    val token: String,
    val user: User
)

data class User(
    val id: String,
    val username: String,
    val phoneNumber: String,
    val role: String,
    var state: String,
    var car: Car? = null
)

data class Car(
    val model: String,
    val color: String,
    val regNumber: String
)

data class CreateTrackResponse(
    val newDriverState:String,
    val newPassengerState: String?,
    val track:Track,
)

data class Track(
    val id: String,
    var startLocation: Location,
    var endLocation: Location,
    val maxSeats: Int,
    val departureTime: String,
    val driverComment: String,
    val passengerComment:String,
    val state: String,
    val driver: String,
    val passenger: String?
)

data class Location(
    val coordinates: Array<Double>,
    val address: String = ""
):Serializable

data class DeleteTrackResponse(
    val newDriverState: String,
    val newPassengerState: String,
    val track: Track,
)

data class TrackListingPage(
    val totalCount:Int,
    val isMore:Boolean,
    val tracks: ArrayList<TrackExpanded>
)

data class TrackExpanded (
    val id: String,
    var startLocation: Location,
    var endLocation: Location,
    val maxSeats: Int,
    val departureTime: String = "",
    val driverComment: String = "",
    val passengerComment:String = "",
    val state: String,
    val driver: String = "",
    val passenger: String? = "",
    val startDistance: Double,
    val endDistance: Double,
    val totalDistance: Double,
    val driverName: String = "",
    val driverPhone: String = "",
):Serializable





