package sk.fri.uniza.photowalk.Friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * je to model viazany na zivotny cyklus celej aktivity a uchovava informaciu o id hlavneho uctu a
 * o indexe vybranej zalozky
 */
class FriendProfileActivityViewModel : ViewModel() {
    private var _mainAccountId = MutableLiveData<Int>()
    val mainAccountId : LiveData<Int>
        get() = _mainAccountId

    private var _tabIndex = MutableLiveData<Int>()
    val tabIndex : LiveData<Int>
        get() = _tabIndex

    /**
     * nastavenie atributu _mainAccountId
     *
     * @param mainAccountId id hlavneho uctu
     */
    fun setMainAccountId(mainAccountId: Int) {
        _mainAccountId.value = mainAccountId
    }

    /**
     * nastavenie atributu _tabIndex
     *
     * @param index index zalozky
     */
    fun setTabIndex(index: Int) {
        _tabIndex.value = index
    }

    init {
        _tabIndex.value = 0
    }
}