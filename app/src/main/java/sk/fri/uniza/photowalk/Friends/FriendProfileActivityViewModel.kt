package sk.fri.uniza.photowalk.Friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FriendProfileActivityViewModel : ViewModel() {
    private var _tabIndex = MutableLiveData<Int>()
    val tabIndex : LiveData<Int>
        get() = _tabIndex

    fun setTabIndex(index: Int) {
        _tabIndex.value = index
    }

    init {
        _tabIndex.value = 0
    }
}