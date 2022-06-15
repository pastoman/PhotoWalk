package sk.fri.uniza.photowalk.Account

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sk.fri.uniza.photowalk.Database.AppDatabase

class AccountViewModel : ViewModel() {
    private var _id = MutableLiveData<Int>()
    val id : LiveData<Int>
        get() = _id

    fun setId(id : Int) {
        _id.value = id
    }

}