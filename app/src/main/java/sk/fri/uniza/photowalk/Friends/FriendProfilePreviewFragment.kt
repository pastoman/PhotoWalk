package sk.fri.uniza.photowalk.Friends

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import sk.fri.uniza.photowalk.Account.AccountViewModel
import sk.fri.uniza.photowalk.Database.AppDatabase
import sk.fri.uniza.photowalk.R
import sk.fri.uniza.photowalk.Util.Util
import sk.fri.uniza.photowalk.databinding.FriendProfilePreviewFragmentBinding

/**
 * Fragment zobrazi dodatocne informacie o ucte priatela
 *
 */
class FriendProfilePreviewFragment : Fragment() {

    private lateinit var binding: FriendProfilePreviewFragmentBinding
    private lateinit var database: AppDatabase

    /**
     * sluzi na vytvorenie komponentov rozhrania pohladu
     *
     * @param inflater sluzi na vytvorenie pohladu z xml layout suboru
     * @param container specialny pohlad, v ktorom je tento pohlad ulozeny
     * @param savedInstanceState ulozeny predchadzajuci stav pri behu aplikacie
     * @return pohlad, ktory je sucatou tohto fragmentu
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.friend_profile_preview_fragment, container, false)

        return binding.root
    }

    /**
     * metoda sa vola hned po metode OnCreateView
     *
     * @param view pohlad vytvoreny metodou onCreateView
     * @param savedInstanceState ulozeny predchadzajuci stav pri behu aplikacie
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = AppDatabase.getDatabase(requireContext())

        loadInfo()
        val friendViewModel = ViewModelProvider(requireActivity())[FriendProfileActivityViewModel::class.java]
        friendViewModel.setTabIndex(TAB_INDEX)
    }

    private fun loadInfo() {
        viewLifecycleOwner.lifecycleScope.launch  {
            try {
                val model = ViewModelProvider(requireActivity()).get(AccountViewModel::class.java)
                val result = database.userDataDao().getData(model.id.value!!)
                val picture = Util.convertByteArrayToBitmap(result!!.picture!!)
                binding.profilePicture.setImageBitmap(picture)
                binding.profileNameValue.text = result.name
                binding.profileSurnameValue.text = result.surname
                binding.profileBirthdayValue.text = result.birthday
                binding.profileCountryValue.text = result.country
            } catch (e : Exception) {
                Toast.makeText(requireContext(), e.message,
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        private const val TAB_INDEX = 1
    }
}