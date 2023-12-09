package com.example.coursework.screens.map

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.geometry.Point
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*

class MapSelectRouteViewModel : ViewModel() {

     val route = MutableLiveData<DrivingRoute?>()




}