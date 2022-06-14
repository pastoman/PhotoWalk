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

    init {
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

    fun setPicture(picture: Picture) {
        _picture.value = picture
    }
}