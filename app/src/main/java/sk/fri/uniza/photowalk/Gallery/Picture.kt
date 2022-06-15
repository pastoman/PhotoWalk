package sk.fri.uniza.photowalk.Gallery

import android.graphics.Bitmap
import java.util.*

data class Picture(val pictureId: Int, val picture : Bitmap, val latitude: Double, val longitude: Double, val date: Date)
