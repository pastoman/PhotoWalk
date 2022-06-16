package sk.fri.uniza.photowalk.Gallery

import android.graphics.Bitmap

/**
 * datova trieda uchovava informacie o obrazku
 *
 * @property pictureId id obrazku
 * @property picture obrazok reprezentovany instanciou triedy Bitmap
 * @property latitude zemepisna sirka
 * @property longitude zemepisna dlzka
 * @property date datum odfotenia obrazku
 */
data class Picture(
    val pictureId: Int,
    val picture : Bitmap,
    val latitude: Double,
    val longitude: Double,
    val date: String
    )
