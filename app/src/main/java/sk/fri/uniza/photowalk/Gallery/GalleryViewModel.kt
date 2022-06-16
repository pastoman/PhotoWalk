package sk.fri.uniza.photowalk.Gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sk.fri.uniza.photowalk.Database.AppDatabase

/**
 * model viazany na zivotny cyklus celej aktivity a uchovava informaciu o obrazkoch pre
 * recyclerView, o zvolenom obrazku, o moznosti mazania obrazku a o tom, ci bol obrazok zvoleny z
 * mapy alebo z galerie
 *
 */
class GalleryViewModel : ViewModel() {

    private var _pictures = mutableListOf<Picture>()
    private var _picturesLiveData = MutableLiveData<List<Picture>>()
    val pictures : LiveData<List<Picture>>
        get() = _picturesLiveData

    private var _picture = MutableLiveData<Picture>()
    val picture : LiveData<Picture>
        get() = _picture

    private var _editable = MutableLiveData<Boolean>()
    val editable : LiveData<Boolean>
        get() = _editable

    private var _fromMap = MutableLiveData<Boolean>()
    val fromMap : LiveData<Boolean>
        get() = _fromMap

    init {
        _editable.value = false
        _picturesLiveData.value = _pictures
    }

    /**
     * pridanie obrazku do zoznamu _pictures a nastavenie atributu _picturesLiveData
     *
     * @param picture instancia datovej triedy Picture
     */
    fun addPicture(picture: Picture) {
        _pictures.add(picture)
        _picturesLiveData.value = _pictures
    }

    /**
     * odstranenie obrazku zo zoznamu _pictures a nastavenie atributu _picturesLiveData
     *
     * @param picture instancia datovej triedy Picture
     */
    fun removePicture(picture: Picture) {
        _pictures.remove(picture)
        _picturesLiveData.value = _pictures
    }

    /**
     * odstranenie vsetkych obrazkov zo zoznamu _pictures a nastavenie atributu _picturesLiveData
     *
     */
    fun clearPictures() {
        _pictures.clear()
        _picturesLiveData.value = _pictures
    }

    /**
     * nastavenie hodnoty atributu _editable
     *
     * @param editable urcuje, ci je mozne obrazok vymazat
     */
    fun setEditable(editable: Boolean) {
        _editable.value = editable
    }

    /**
     * nastavenie hodnoty atributu _picture
     *
     * @param picture instancia datovej triedy Picture
     */
    fun setPicture(picture: Picture) {
        _picture.value = picture
    }

    /**
     * nastavenie hodnoty atributu _fromMap
     *
     * @param fromMap urcuje, ci bol obrazok vybrany z mapy
     */
    fun setFromMap(fromMap: Boolean) {
        _fromMap.value = fromMap
    }
}