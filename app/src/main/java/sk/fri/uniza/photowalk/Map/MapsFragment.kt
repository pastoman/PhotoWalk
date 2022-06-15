package sk.fri.uniza.photowalk.Map

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.maps.route.extensions.drawRouteOnMap
import com.maps.route.extensions.moveCameraOnMap
import com.maps.route.model.TravelMode
import kotlinx.coroutines.launch
import sk.fri.uniza.photowalk.Account.AccountViewModel
import sk.fri.uniza.photowalk.BuildConfig
import sk.fri.uniza.photowalk.Database.AppDatabase
import sk.fri.uniza.photowalk.Database.UserPictures
import sk.fri.uniza.photowalk.Friends.MainActivityViewModel
import sk.fri.uniza.photowalk.Gallery.GalleryFragment
import sk.fri.uniza.photowalk.Gallery.GalleryViewModel
import sk.fri.uniza.photowalk.Gallery.Picture
import sk.fri.uniza.photowalk.Gallery.PicturePreviewFragment
import sk.fri.uniza.photowalk.R
import sk.fri.uniza.photowalk.Util.Util
import sk.fri.uniza.photowalk.databinding.MapsFragmentBinding
import java.util.concurrent.ConcurrentHashMap


class MapsFragment : Fragment(), Timer.OnFinishListener, OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {




    private lateinit var binding: MapsFragmentBinding
    private lateinit var placesClient: PlacesClient
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var database: AppDatabase
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var mainViewModel: MainActivityViewModel
    private val timer = Timer(this)
    private var mMap: GoogleMap? = null
    private val defaultLocation = LatLng(49.222229, 18.740134)
    private var locationPermissionGranted = false
    private lateinit var markers: Markers
    private var lastKnownLocation: Location? = null

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        googleMap.setOnInfoWindowClickListener(this)

        // Construct a PlacesClient
        getLocationPermission()



        markers = Markers(mMap!!, accountViewModel.id.value!!, database)
        viewLifecycleOwner.lifecycleScope.launch {
            markers.initializeGalleryViewModel(requireActivity())
        }
        val galleryViewModel = ViewModelProvider(requireActivity())[GalleryViewModel::class.java]
        galleryViewModel.pictures.observe(requireActivity()) {
            lifecycleScope.launch {
                markers.updateMarkers()
            }
        }

        timer.startTimer()
//        if (lastKnownLocation != null) {
//            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                LatLng(lastKnownLocation!!.latitude,
//                    lastKnownLocation!!.longitude), DEFAULT_ZOOM.toFloat()))
//            val source = LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude) //starting point (LatLng)
//            val destination = defaultLocation // ending point (LatLng)

//            mMap?.run {
//                moveCameraOnMap(latLng = source) // if you want to zoom the map to any point
//
//                //Called the drawRouteOnMap extension to draw the polyline/route on google maps
//                drawRouteOnMap(
//                    getString(R.string.maps_api_key), //your API key
//                    source = source, // Source from where you want to draw path
//                    destination = destination, // destination to where you want to draw path
//                    context = requireActivity().application //Activity context
//                )
//            }
        //}

    }

    override fun onInfoWindowClick(marker: Marker) {
        val galleryViewModel = ViewModelProvider(requireActivity())[GalleryViewModel::class.java]
        viewLifecycleOwner.lifecycleScope.launch {
            val result = database.userPicturesDao().getPicture(marker.title!!.toInt())
            val picture = Picture(
                result.id_picture,
                Util.convertByteArrayToBitmap(result.picture),
                result.latitude,
                result.longitude,
                Util.StringToDate(result.date)
            )
            if (result.id_account == accountViewModel.id.value!!) {
                galleryViewModel.setPicture(picture)
                galleryViewModel.setFromMap(true)
                galleryViewModel.setEditable(true)
                val ft: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                ft.replace(R.id.mainFragment, PicturePreviewFragment())
                ft.addToBackStack(null)
                ft.commit()
            } else {
                galleryViewModel.setPicture(picture)
                galleryViewModel.setFromMap(true)
                galleryViewModel.setEditable(false)
                val ft: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                ft.replace(R.id.mainFragment, PicturePreviewFragment())
                ft.addToBackStack(null)
                ft.commit()
            }

        }
    }

    override fun onTimerFinish() {
       viewLifecycleOwner.lifecycleScope.launch {
           markers.updateMarkers()
           timer.startTimer()
       }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.maps_fragment, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainViewModel.setPosition(null)
        timer.stopTimer()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = AppDatabase.getDatabase(requireContext())
        accountViewModel = ViewModelProvider(requireActivity())[AccountViewModel::class.java]
        mainViewModel = ViewModelProvider(requireActivity())[MainActivityViewModel::class.java]
        mainViewModel.setTabIndex(TAB_INDEX)
        Places.initialize(requireContext(), BuildConfig.MAPS_API_KEY)
        placesClient = Places.createClient(requireContext())

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        binding.takePhoto.setOnClickListener {

            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } catch (e: ActivityNotFoundException) {
             // display error state to the user
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {
            try {
                val image = data!!.extras!!.get("data") as Bitmap
                val userPicture = UserPictures(
                    0,
                    accountViewModel.id.value!!,
                    Util.convertBitmapToByteArray(image),
                    lastKnownLocation!!.latitude,
                    lastKnownLocation!!.longitude,
                    Util.CurrentDateInString()
                )
                val galleryViewModel = ViewModelProvider(requireActivity())[GalleryViewModel::class.java]
                viewLifecycleOwner.lifecycleScope.launch {
                    database.userPicturesDao().addPicture(userPicture)
                    markers.updateMarkers()
                    markers.initializeGalleryViewModel(requireActivity())
                }
            } catch (e : Exception) {
                Toast.makeText(requireContext(), e.message,
                    Toast.LENGTH_LONG).show()
            }

        }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {
        if (mMap == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                mMap?.isMyLocationEnabled = true
                mMap?.uiSettings?.isMyLocationButtonEnabled = true
                mMap?.uiSettings?.isMapToolbarEnabled = true
                mMap?.uiSettings?.isRotateGesturesEnabled = true
                mMap?.uiSettings?.isZoomControlsEnabled = true
                mainViewModel.position.observe(viewLifecycleOwner) {
                    if (it != null) {
                        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            it,
                            DEFAULT_ZOOM.toFloat()))
                    } else {
                        getDeviceLocation()
                    }
                }
            } else {
                mMap?.isMyLocationEnabled = false
                mMap?.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            LatLng(lastKnownLocation!!.latitude,
                            lastKnownLocation!!.longitude), DEFAULT_ZOOM.toFloat()))
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        mMap?.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
                        mMap?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(requireActivity().application,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
            updateLocationUI()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                    // Turn on the My Location layer and the related control on the map.
                    updateLocationUI()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }

    }

    companion object {
        private val TAG = MapsFragment::class.java.simpleName
        private const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

        // Keys for storing activity state.
        // [START maps_current_place_state_keys]
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
        // [END maps_current_place_state_keys]

        // Used for selecting the current place.
        private const val M_MAX_ENTRIES = 5

        private const val REQUEST_IMAGE_CAPTURE = 2
        private const val TAB_INDEX = 0
    }
}