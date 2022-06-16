package sk.fri.uniza.photowalk.Friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

/**
 * viewModel viazany na zivotny cyklus celej aktivity a uchovava informaciu o pozicii obrazku a o
 * indexe aktualnej zalozky
 *
 */
class MainActivityViewModel : ViewModel() {

    private var _position = MutableLiveData<LatLng?>()
    val position : LiveData<LatLng?>
        get() = _position

    private var _tabIndex = MutableLiveData<Int>()
    val tabIndex : LiveData<Int>
        get() = _tabIndex

    init {
        _tabIndex.value = 0
        _position.value = null
    }

    fun setTabIndex(index: Int) {
        _tabIndex.value = index
    }

    fun setPosition(position: LatLng?) {
        _position.value = position
    }
}