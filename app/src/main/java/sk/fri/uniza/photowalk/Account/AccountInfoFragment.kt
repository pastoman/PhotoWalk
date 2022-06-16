package sk.fri.uniza.photowalk.Account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import kotlinx.coroutines.launch
import sk.fri.uniza.photowalk.Database.AppDatabase
import sk.fri.uniza.photowalk.Login.LoginActivity
import sk.fri.uniza.photowalk.R
import sk.fri.uniza.photowalk.Util.Util
import sk.fri.uniza.photowalk.databinding.AccountInfoFragmentBinding

/**
 * Fragment zobrazi dodatocne informacie o ucte, ktore vsak nie su dostupne k editacii
 *
 */
class AccountInfoFragment : Fragment() {

    lateinit var binding: AccountInfoFragmentBinding
    lateinit var database: AppDatabase

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
        binding = DataBindingUtil.inflate(inflater, R.layout.account_info_fragment, container, false)

        database = AppDatabase.getDatabase(requireContext())

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
        loadInfo()

        binding.edit.setOnClickListener {
            it.findNavController().navigate(R.id.action_accountInfoFragment_to_accountEditFragment)
        }

        binding.logout.setOnClickListener {
            requireActivity().finish()
            val intent = Intent(it.context, LoginActivity::class.java)
            startActivity(intent)
        }

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


}