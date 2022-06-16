package sk.fri.uniza.photowalk.Friends

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import sk.fri.uniza.photowalk.Account.AccountViewModel
import sk.fri.uniza.photowalk.Database.AppDatabase
import sk.fri.uniza.photowalk.Gallery.GalleryFragment
import sk.fri.uniza.photowalk.R
import sk.fri.uniza.photowalk.databinding.FriendsListFragmentBinding
import java.lang.Exception

/**
 * Fragment vyuziva recyclerView na zobrazenie zoznamu priatelov a taktiez umoznuje pridavat
 * priatelov na zaklade nazvu ich uctu
 *
 */
class FriendsListFragment : Fragment() {
    private lateinit var binding: FriendsListFragmentBinding
    private lateinit var database: AppDatabase
    private lateinit var viewModel: AccountViewModel
    private val friends = mutableListOf<Friend>()

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
        binding = DataBindingUtil.inflate(inflater, R.layout.friends_list_fragment, container, false)
        database = AppDatabase.getDatabase(requireContext())
        viewModel = ViewModelProvider(requireActivity())[AccountViewModel::class.java]
        val mainViewModel =ViewModelProvider(requireActivity())[MainActivityViewModel::class.java]
        mainViewModel.setTabIndex(TAB_INDEX)
        binding.addFriendButton.setOnClickListener {
            showFriendDialog()
        }
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

        loadFriends()

    }

    /**
     * Nacita priatelov do recycler view z databazy
     *
     */
    fun loadFriends() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                friends.clear()
                val result = database.friendDao().getAllFriends(viewModel.id.value!!)

                for (item in result) {
                    friends.add(Friend(item.friendId, database.accountDao().findUsername(item.friendId)!!.username))
                }
                binding.friendList.adapter = FriendsRecyclerViewAdapter(friends,
                    viewModel.id.value!!, this@FriendsListFragment)
                binding.friendList.layoutManager = LinearLayoutManager(requireContext())
            } catch (e : Exception) {
                Toast.makeText(requireContext(), e.message,
                    Toast.LENGTH_LONG).show()
            }


        }
    }

    private fun showFriendDialog() {
        // zdroj: https://handyopinion.com/show-alert-dialog-with-an-input-field-edittext-in-android-kotlin/
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Title")

        // Set up the input
        val input = EditText(requireContext())
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setHint("Enter username")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            // Here you get get input text from the Edittext
            viewLifecycleOwner.lifecycleScope.launch {

                val result = database.accountDao().findFriendId(input.text.toString())
                if (result != null) {
                    database.friendDao().addFriend(
                        sk.fri.uniza.photowalk.Database.Friend(viewModel.id.value!!, result.id))
                    loadFriends()
                } else {
                    Toast.makeText(requireContext(), "Unable to find user with such name",
                        Toast.LENGTH_LONG).show()
                }
            }

        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
    }

    companion object {
        private const val TAB_INDEX = 2
    }
}