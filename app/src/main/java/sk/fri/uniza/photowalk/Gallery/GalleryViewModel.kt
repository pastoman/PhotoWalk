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

    fun clear() {
        _pictures.clear()
        _picturesLiveData.value = _pictures
    }
}