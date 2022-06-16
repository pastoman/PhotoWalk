package sk.fri.uniza.photowalk.Map

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import sk.fri.uniza.photowalk.Account.AccountViewModel
import sk.fri.uniza.photowalk.Database.AppDatabase
import sk.fri.uniza.photowalk.Database.UserPictures
import sk.fri.uniza.photowalk.Gallery.GalleryViewModel
import sk.fri.uniza.photowalk.Gallery.Picture
import sk.fri.uniza.photowalk.Util.Util
import java.util.concurrent.ConcurrentHashMap

/**
 * trieda, ktora ma na starost manipulaciu so znackami na mape
 *
 * @property map instancia google map
 * @property accountId id uctu
 * @property database aplikacna databaza
 */
class Markers(private val map: GoogleMap,
              private val accountId: Int,
              private val database: AppDatabase
              ) {
    private val markers = ConcurrentHashMap<Marker, String>()


    /**
     * aktualizacia znaciek na mape
     *
     */
    suspend fun updateMarkers() {
        removeMarkers()
        placeMarkers()
    }

    /**
     * aktualizuje viewModel typu GalleryViewModel tak, ze ho premaze a
     * do neho  prida vsetky obrazky z databazy
     *
     * @param activity aktivita, ktora je momentalne aktivna
     */
    suspend fun updateGalleryViewModel(activity: FragmentActivity) {
        val viewModel = ViewModelProvider(activity)[GalleryViewModel::class.java]
        val database = AppDatabase.getDatabase(activity)
        viewModel.setFromMap(false)
        viewModel.setEditable(true)
        viewModel.clearPictures()
        val model = ViewModelProvider(activity)[AccountViewModel::class.java]
        val result = database.userPicturesDao().getAllPictures(model.id.value!!)
        if (result.isNotEmpty()) {
            for (item in result) {
                viewModel.addPicture(
                    Picture(
                        item.id_picture,
                        Util.convertByteArrayToBitmap(item.picture),
                        item.latitude,
                        item.longitude,
                        item.date
                    )
                )
            }
        }
    }

    private suspend fun placeMarkers() {
        var picturesResult = database.userPicturesDao().getAllPictures(accountId)
        var accountResult = database.accountDao().findUsername(accountId)
        for (picture in picturesResult) {
            addMarker(accountResult!!.username ,picture)
        }
        val friendResult = database.friendDao().getAllFriends(accountId)
        for (item in friendResult) {
            accountResult = database.accountDao().findUsername(item.friendId)
            picturesResult = database.userPicturesDao().getAllPictures(item.friendId)
            for (picture in picturesResult) {
                addMarker(accountResult!!.username, picture)
            }
        }
    }

    private fun addMarker(username: String, picture: UserPictures) {
        val position = LatLng(picture.latitude, picture.longitude)
        val marker = MarkerOptions()
            .position(position)
            .title(picture.id_picture.toString())
            .snippet(username) // pocet navstivení
            .icon(
                BitmapDescriptorFactory.fromBitmap(
                Bitmap.createScaledBitmap(
                    Util.convertByteArrayToBitmap(picture.picture),
                    100, 100, false))
            )
        map.addMarker(marker)
    }

    private fun removeMarkers() {
        markers.clear()
        map.clear()
    }


}