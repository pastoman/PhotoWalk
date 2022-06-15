package sk.fri.uniza.photowalk.Util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ArrayAdapter
import sk.fri.uniza.photowalk.R
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.min
import kotlin.math.roundToInt

class Util {
    companion object {
        fun convertBitmapToByteArray(picture: Bitmap, maxResolution: Int): ByteArray {
            val stream = ByteArrayOutputStream()
            val ratio: Float = min(
                maxResolution.toFloat() / picture.width,
                maxResolution.toFloat() / picture.height
            )
            val width =
                (ratio * picture.width).roundToInt()
            val height =
                (ratio * picture.height).roundToInt()

            val resizedBitmap = Bitmap.createScaledBitmap(
                picture, width,
                height, true
            )
            resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            return stream.toByteArray()
        }

        @Suppress("DEPRECATION")
        fun convertByteArrayToBitmap(byteArray: ByteArray): Bitmap {
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }

        fun StringToDate(string: String) : Date {
            val formatter = SimpleDateFormat("dd.MM.yyyy':' HH:mm:ss z")
            val text = "2022-01-06"
            return formatter.parse(string)
        }

        fun DateToString(date: Date) : String {
            return SimpleDateFormat("dd.MM.yyyy':' HH:mm:ss z").format(Date())
        }

        fun CurrentDateInString() : String {
            val date = Calendar.getInstance().time
            return SimpleDateFormat("dd.MM.yyyy':' HH:mm:ss z").format(date)
        }
    }
}