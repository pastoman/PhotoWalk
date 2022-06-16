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
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.launch
import sk.fri.uniza.photowalk.Account.AccountViewModel
import sk.fri.uniza.photowalk.BuildConfig
import sk.fri.uniza.photowalk.Database.AppDatabase
import sk.fri.uniza.photowalk.Database.UserPictures
import sk.fri.uniza.photowalk.Friends.MainActivityViewModel
import sk.fri.uniza.photowalk.Gallery.GalleryViewModel
import sk.fri.uniza.photowalk.Gallery.Picture
import sk.fri.uniza.photowalk.Gallery.PicturePreviewFragment
import sk.fri.uniza.photowalk.R
import sk.fri.uniza.photowalk.Util.Util
import sk.fri.uniza.photowalk.databinding.MapsFragmentBinding

/**
 * fragment predstavuje nahlad na mapy od googlu, na ktorej je zobrazena lokacia pouzivatela a
 * jeho fotky a aj fotky jeho priatelov
 *
 */
class MapsFragment : Fragment(), Timer.OnFinishListener, OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private lateinit var binding: MapsFragmentBinding
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

    /**
     * sluzi na vytvorenie komponentov rozhrania pohladu
     *
     * @param inflater sluzi na vytvorenie pohladu z xml layout suboru
     * @param container specialny pohlad, v ktorom je tento pohlad ulozeny
     * @param savedInstanceState ulozeny predchadzajuci stav pri behu aplikacie
     * @return pohlad, ktory je sucatou tohto fragmentu
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.maps_fragment, container, false)

        return binding.root
    }

    /**
     * metoda sa vola hned po metode OnCreateView
     *
     * @param view pohlad vytvoreny metodou onCreateView
     * @param savedInstanceState ulozeny predchadzajuci stav pri behu aplikacie
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = AppDatabase.getDatabase(requireContext())
        accountViewModel = ViewModelProvider(requireActivity())[AccountViewModel::class.java]
        mainViewModel = ViewModelProvider(requireActivity())[MainActivityViewModel::class.java]
        mainViewModel.setTabIndex(TAB_INDEX)
        Places.initialize(requireContext(), BuildConfig.MAPS_API_KEY)

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

    /**
     * metoda, ktora sa zavola pri zniceni pohladu, zastavuje casovac aktualizacie mapy
     *
     */
    override fun onDestroyView() {
        super.onDestroyView()
        timer.stopTimer()
    }

    /**
     * funkcia sa zavola, ked su mapy od google pripravene
     *
     * @param googleMap google mapy
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        googleMap.setOnInfoWindowClickListener(this)

        // Construct a PlacesClient
        getLocationPermission()



        markers = Markers(mMap!!, accountViewModel.id.value!!, database)
        viewLifecycleOwner.lifecycleScope.launch {
            markers.updateGalleryViewModel(requireActivity())
        }
        val galleryViewModel = ViewModelProvider(requireActivity())[GalleryViewModel::class.java]
        galleryViewModel.pictures.observe(this) {
            lifecycleScope.launch {
                markers.updateMarkers()
            }
        }
        timer.startTimer()
    }

    /**
     * metoda sa zavola po kliknuti na stitok znacky na mape
     *
     * @param marker znacka na mape
     */
    override fun onInfoWindowClick(marker: Marker) {
        viewLifecycleOwner.lifecycleScope.launch {
            val result = database.userPicturesDao().getPicture(marker.title!!.toInt())
            val picture = Picture(
                result.id_picture,
                Util.convertByteArrayToBitmap(result.picture),
                result.latitude,
                result.longitude,
                result.date
            )
            if (result.id_account == accountViewModel.id.value!!) {
                openPicturePreview(picture, true)
            } else {
                openPicturePreview(picture, false)
            }

        }
    }

    /**
     * zavola sa, ked vyprsi casovac a sluzi na aktualizaciu znaciek na mape a resetovanie casovaca
     *
     */
    override fun onTimerFinish() {
        viewLifecycleOwner.lifecycleScope.launch {
            markers.updateMarkers()
            timer.startTimer()
        }
    }

    /**
     * Vyhodnotenie vysledku systemovych poziadaviek a intentov
     *
     * @param requestCode specificky kod nasej poziadavky
     * @param resultCode kod vysledku spracovania poziadavky
     * @param data data, ktore vratila poziadavka
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {
            try {
                val image = data!!.extras!!.get("data") as Bitmap
                val userPicture = UserPictures(
                    0,
                    accountViewModel.id.value!!,
                    Util.convertBitmapToByteArray(image,1200),
                    lastKnownLocation!!.latitude,
                    lastKnownLocation!!.longitude,
                    Util.CurrentDateInString()
                )
                viewLifecycleOwner.lifecycleScope.launch {
                    database.userPicturesDao().addPicture(userPicture)
                    markers.updateMarkers()
                    markers.updateGalleryViewModel(requireActivity())
                }
            } catch (e : Exception) {
                Toast.makeText(requireContext(), e.message,
                    Toast.LENGTH_LONG).show()
            }

        }
    }

    /**
     * Vyhodnotenie vysledku ziadosti o ziskanie opravnenia aplikacie
     *
     * @param requestCode specificky kod nasej poziadavky
     * @param permissions popisy opravneni
     * @param grantResults vysledok poziadavky o ziskanie opravnenia
     */
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

                }
                // Turn on the My Location layer and the related control on the map.
                updateLocationUI()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }


    }

    private fun openPicturePreview(picture: Picture, editable: Boolean) {
        val galleryViewModel = ViewModelProvider(requireActivity())[GalleryViewModel::class.java]
        galleryViewModel.setPicture(picture)
        galleryViewModel.setFromMap(true)
        galleryViewModel.setEditable(editable)
        val ft: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        ft.replace(R.id.mainFragment, PicturePreviewFragment())
        ft.addToBackStack(null)
        ft.commit()
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
                if (mainViewModel.position.value != null) {
                    mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        mainViewModel.position.value!!,
                        DEFAULT_ZOOM.toFloat()))
                    mainViewModel.setPosition(null)
                } else {
                    getDeviceLocation()
                }
                mainViewModel.position.observe(this) {
                    if (it != null) {
                        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            it,
                            DEFAULT_ZOOM.toFloat()))
                        mainViewModel.setPosition(null)
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
         * zdroj: https://developers.google.com/maps/documentation/android-sdk/current-place-tutorial
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
                        if (lastKnownLocation != null) {
                            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                LatLng(lastKnownLocation!!.latitude,
                                    lastKnownLocation!!.longitude), DEFAULT_ZOOM.toFloat()))
                        } else {
                            mMap?.moveCamera(CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        mMap?.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
                        mMap?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            } else {
                mMap?.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun getLocationPermission() {
        /*
         * zdroj: https://developers.google.com/maps/documentation/android-sdk/location
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


    companion object {
        private val TAG = MapsFragment::class.java.simpleName
        private const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

        private const val REQUEST_IMAGE_CAPTURE = 2
        private const val TAB_INDEX = 0
    }
}