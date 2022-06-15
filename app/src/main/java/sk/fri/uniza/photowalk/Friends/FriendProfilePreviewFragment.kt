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


class FriendProfilePreviewFragment : Fragment() {

    private lateinit var binding: FriendProfilePreviewFragmentBinding
    private lateinit var database: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.friend_profile_preview_fragment, container, false)

        return binding.root
    }

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
                val picture = Util.convertByteArrayToBitmap(result[0].picture!!)
                binding.profilePicture.setImageBitmap(picture)
                binding.profileNameValue.text = result[0].name
                binding.profileSurnameValue.text = result[0].surname
                binding.profileBirthdayValue.text = result[0].birthday
                binding.profileCountryValue.text = result[0].country
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