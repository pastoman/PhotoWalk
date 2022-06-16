package sk.fri.uniza.photowalk.Account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * model viazany na zivotny cyklus celej aktivity a uchovava informaciu o id prihlaseneho
 * uctu, popripade pri prehliadani uctu priatela predstavuje jeho id
 *
 */
class AccountViewModel : ViewModel() {
    private var _id = MutableLiveData<Int>()
    val id : LiveData<Int>
        get() = _id

    /**
     * nastavenie hodnoty atributu _id
     *
     * @param id hodnota id, ktoru chceme nastavit
     */
    fun setId(id : Int) {
        _id.value = id
    }

}