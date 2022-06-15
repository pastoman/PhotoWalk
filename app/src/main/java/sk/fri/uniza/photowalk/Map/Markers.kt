package sk.fri.uniza.photowalk.Map

import android.graphics.Bitmap
import androidx.lifecycle.ViewModelProvider
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
import sk.fri.uniza.photowalk.Util.Util
import java.util.concurrent.ConcurrentHashMap

class Markers(private val map: GoogleMap, private val accountId: Int, private val database: AppDatabase) {
    private val markers = ConcurrentHashMap<Marker, String>()

    suspend fun placeMarkers() {
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

    suspend fun updateMarkers() {
        removeMarkers()
        placeMarkers()
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