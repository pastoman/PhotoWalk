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
        /**
         * Medota prekonvertuje instanciu triedy Bitmap na pole bytov
         *
         * @param picture bitmapa, ktoru chceme prekonvertovat
         * @param maxResolution maximalne rozlisenie obrazku
         * @return pole bytov predstavujuce obrazok
         */
        fun convertBitmapToByteArray(picture: Bitmap, maxResolution: Int): ByteArray {
            // zdroj: https://stackoverflow.com/questions/8232608/fit-image-into-imageview-keep-aspect-ratio-and-then-resize-imageview-to-image-d
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

        /**
         * Medota prekonvertuje pole bytov na instanciu triedy Bitmap
         *
         * @param byteArray pole bytov predstavujuce obrazok
         * @return instancia treidy Bitmap
         */
        @Suppress("DEPRECATION")
        fun convertByteArrayToBitmap(byteArray: ByteArray): Bitmap {
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }

        /**
         * metoda vrati momentalny cas a datum vo forme stringu
         *
         * @return datum vo forme stringu
         */
        fun CurrentDateInString() : String {
            val date = Calendar.getInstance().time
            return SimpleDateFormat("dd.MM.yyyy':' HH:mm:ss z").format(date)
        }
    }
}