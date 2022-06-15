package sk.fri.uniza.photowalk.Friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FriendProfileActivityViewModel : ViewModel() {
    private var _mainAccountId = MutableLiveData<Int>()
    val mainAccountId : LiveData<Int>
        get() = _mainAccountId

    private var _tabIndex = MutableLiveData<Int>()
    val tabIndex : LiveData<Int>
        get() = _tabIndex


    fun setMainAccountId(mainAccountId: Int) {
        _mainAccountId.value = mainAccountId
    }
    fun setTabIndex(index: Int) {
        _tabIndex.value = index
    }

    init {
        _tabIndex.value = 0
    }
}