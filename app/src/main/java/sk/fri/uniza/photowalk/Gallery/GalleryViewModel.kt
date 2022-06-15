package sk.fri.uniza.photowalk.Gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sk.fri.uniza.photowalk.Database.AppDatabase

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

    fun addPicture(picture: Picture) {
        _pictures.add(picture)
        _picturesLiveData.value = _pictures
    }

    fun removePicture(picture: Picture) {
        _pictures.remove(picture)
        _picturesLiveData.value = _pictures
    }

    fun clearPictures() {
        _pictures.clear()
        _picturesLiveData.value = _pictures
    }

    fun setEditable(editable: Boolean) {
        _editable.value = editable
    }

    fun setPicture(picture: Picture) {
        _picture.value = picture
    }

    fun setFromMap(fromMap: Boolean) {
        _fromMap.value = fromMap
    }
}