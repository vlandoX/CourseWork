package com.example.coursework.screens.map

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.coursework.MainActivity
import com.example.coursework.R
import com.example.coursework.databinding.FragmentMapSelectRouteBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingSession.DrivingRouteListener
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.location.FilteringMode
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationManager
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.runtime.Error
import java.io.IOException
import java.util.*


class MapSelectRouteFragment : Fragment() {

    private val TAG = MainActivity::class.java.simpleName
    private val DESIRED_ACCURACY = 0.0
    private val MINIMAL_TIME: Long = 1000
    private val MINIMAL_DISTANCE = 1.0
    private val USE_IN_BACKGROUND = false
    val COMFORTABLE_ZOOM_LEVEL = 18f
    private val MAPKIT_API_KEY = ""
    private val mapView: MapView? = null
    private val rootCoordinatorLayout: CoordinatorLayout? = null
    private var locationManager: LocationManager? = null
    private var myLocationListener: LocationListener? = null
    private var myLocation: Point? = null

    companion object {
        fun newInstance() = MapSelectRouteFragment()
    }

    private var _binding: FragmentMapSelectRouteBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MapSelectRouteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapSelectRouteBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MapSelectRouteViewModel::class.java)

        MapKitFactory.initialize(requireContext())
        DirectionsFactory.initialize(requireContext())
        SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)


        locationManager = MapKitFactory.getInstance().createLocationManager()

        myLocationListener = object : LocationListener {

            override fun onLocationUpdated(p0: com.yandex.mapkit.location.Location) {
                if (myLocation == null) {
                    moveCamera(p0.position, COMFORTABLE_ZOOM_LEVEL)
                }
                myLocation = p0.position //this user point
                Log.w(TAG, "my location - " + myLocation!!.latitude + "," + myLocation!!.longitude)
            }

            override fun onLocationStatusUpdated(locationStatus: LocationStatus) {
                if (locationStatus == LocationStatus.NOT_AVAILABLE) {
                    println("sdncvoadsjv")
                }
            }
        }



        viewModel.route.observe(this) {
            binding.confirmButton.isEnabled = it != null
        }



        binding.confirmButton.setOnClickListener {
            viewModel.route.value.let { route->
                if(route!=null){

                    when (view.findNavController().previousBackStackEntry?.destination?.id) {
                        R.id.routeCreationFragment -> {
                            val action = MapSelectRouteFragmentDirections.actionMapSelectRouteFragmentToRouteCreationFragment(
                                route.geometry.points.first().longitude.toString(),
                                route.geometry.points.first().latitude.toString(),
                                route.geometry.points.last().longitude.toString(),
                                route.geometry.points.last().latitude.toString(),
                                getAddressByCoordinates(route.geometry.points.first()),
                                getAddressByCoordinates(route.geometry.points.last())
                            )
                            this.findNavController().navigate(action)
                        }
                        R.id.creatingRouteFragment -> {
                            val action = MapSelectRouteFragmentDirections.actionMapSelectRouteFragmentToCreatingRouteFragment(
                                route.geometry.points.first().longitude.toString(),
                                route.geometry.points.first().latitude.toString(),
                                route.geometry.points.last().longitude.toString(),
                                route.geometry.points.last().latitude.toString(),
                                getAddressByCoordinates(route.geometry.points.first()),
                                getAddressByCoordinates(route.geometry.points.last())
                            )
                            this.findNavController().navigate(action)
                        }
                        else -> {

                        }
                    }

                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapView.onStart()
        subscribeToLocationUpdate()
        binding.mapView.mapWindow.map.addInputListener(inputListener)
    }

    override fun onStop() {
        myLocationListener?.let { locationManager?.unsubscribe(it) }
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }



    private fun subscribeToLocationUpdate() {
        if (locationManager != null && myLocationListener != null) {
            locationManager!!.subscribeForLocationUpdates(
                DESIRED_ACCURACY,
                MINIMAL_TIME,
                MINIMAL_DISTANCE,
                USE_IN_BACKGROUND,
                FilteringMode.OFF,
                myLocationListener!!
            )
        }
    }

    private fun moveCamera(point: Point, zoom: Float) {
        binding.mapView.mapWindow.map.move(
            CameraPosition(point, zoom, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 1f),
            null
        )
    }


    private var startPlacemark: PlacemarkMapObject? = null
    private var endPlacemark: PlacemarkMapObject? = null


    private val inputListener = object : InputListener {
        override fun onMapTap(p0: Map, p1: Point) {
            p1.let {
                if (startPlacemark == null) {
                    // Set start point
                    startPlacemark = p0.mapObjects.addPlacemark(it)
                    startPlacemark?.opacity = 0.5f
                    startPlacemark?.isDraggable = true
                } else if (endPlacemark == null) {
                    // Set end point
                    endPlacemark = p0.mapObjects.addPlacemark(it)
                    endPlacemark?.opacity = 0.5f
                    endPlacemark?.isDraggable = true
                    displayRoute(startPlacemark!!.geometry, endPlacemark!!.geometry)
                } else {
                    // Clear existing points and set start point
                    p0.mapObjects.clear()
                    startPlacemark = p0.mapObjects.addPlacemark(it)
                    startPlacemark?.opacity = 0.5f
                    startPlacemark?.isDraggable = true
                    endPlacemark = null
                    viewModel.route.value = null
                }
            }
        }

        override fun onMapLongTap(p0: Map, p1: Point) {
            TODO("Not yet implemented")
        }

    }

    private val mapTapListener = object : MapObjectTapListener {

        override fun onMapObjectTap(p0: MapObject, p1: Point): Boolean {
            p1?.let {
                if (startPlacemark == null) {
                    // Set start point
                    startPlacemark = binding.mapView.mapWindow.map.mapObjects.addPlacemark(it)
                    startPlacemark?.opacity = 0.5f
                    startPlacemark?.isDraggable = true
                } else if (endPlacemark == null) {
                    // Set end point
                    endPlacemark = binding.mapView.mapWindow.map.mapObjects.addPlacemark(it)
                    endPlacemark?.opacity = 0.5f
                    endPlacemark?.isDraggable = true
                    displayRoute(startPlacemark!!.geometry, endPlacemark!!.geometry)
                } else {
                    // Clear existing points and set start point
                    binding.mapView.mapWindow.map.mapObjects.clear()
                    startPlacemark = binding.mapView.mapWindow.map.mapObjects.addPlacemark(it)
                    startPlacemark?.opacity = 0.5f
                    startPlacemark?.isDraggable = true
                    endPlacemark = null
                }
                return true
            }
            return false
        }
    }


    private fun displayRoute(startPoint: Point, endPoint: Point) {
        val drivingRouter = DirectionsFactory.getInstance().createDrivingRouter()
        val requestPoints = buildList {
            add(RequestPoint(startPoint, RequestPointType.WAYPOINT, null, null))
            add(RequestPoint(endPoint, RequestPointType.WAYPOINT, null, null))
        }

        val drivingOptions = DrivingOptions()
        val vehicleOptions = VehicleOptions()

        drivingRouter.requestRoutes(
            requestPoints,
            drivingOptions,
            vehicleOptions,
            object : DrivingRouteListener {

                override fun onDrivingRoutes(p0: MutableList<DrivingRoute>) {
                    if (p0.isNotEmpty()) {
                        // Display the first route on the map
                        viewModel.route.value = p0[0]
                        binding.mapView.mapWindow.map.mapObjects.addPolyline(p0[0].geometry)

                        // Move the camera to fit the entire route

                        // binding.mapView.mapWindow.map.move(CameraPosition(cameraPosition.target, 10.0f, 0.0f, 0.0f))
                    }
                }

                override fun onDrivingRoutesError(p0: Error) {
                }
            }
        )
    }

    private fun getAddressByCoordinates(coordinates: Point): String {
        try {
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val addresses = geocoder.getFromLocation(coordinates.latitude, coordinates.longitude, 1)
            return addresses?.get(0)?.getAddressLine(0) ?: "${coordinates.latitude.toFloat()}, ${coordinates.longitude.toFloat()}"
        }catch (e:IOException) {
            return "${coordinates.latitude.toFloat()}, ${coordinates.longitude.toFloat()}"
        }
    }


}