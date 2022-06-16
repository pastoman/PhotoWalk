package sk.fri.uniza.photowalk.Login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import kotlinx.coroutines.launch
import sk.fri.uniza.photowalk.Account.AccountViewModel
import sk.fri.uniza.photowalk.Database.AppDatabase
import sk.fri.uniza.photowalk.MainActivity
import sk.fri.uniza.photowalk.R
import sk.fri.uniza.photowalk.databinding.LoginFragmentBinding


/**
 * Fragment predstavuje prihlasovaciu obrazovku a kontroluje, ci je ucet so zadanym menom a
 * heslom zaregistrovany
 *
 */
class LoginFragment : Fragment() {
    private lateinit var binding: LoginFragmentBinding
    private lateinit var database: AppDatabase
    private lateinit var viewModel: AccountViewModel

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
        // Inflate the layout XML file and return a binding object instance
        binding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false)

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

        database = AppDatabase.getDatabase(requireActivity().application)
        viewModel = ViewModelProvider(requireActivity())[AccountViewModel::class.java]
        binding.LoginButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                if (checkAccount()) {
                    val intent = Intent(it.context, MainActivity::class.java)
                    // zdroj: https://stackoverflow.com/questions/6767596/how-to-pass-intent-extras
                    val extras: Bundle = Bundle()
                    extras.putInt("id", viewModel.id.value!!)
                    extras.putParcelable("position", null)
                    intent.putExtras(extras)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
        }

        binding.RegisterButtonLogin.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }


    private suspend fun checkAccount() : Boolean {
        val result = database.accountDao().checkAccount(binding.usernameLoginBox.text.toString(), binding.passwordLoginBox.text.toString())
        return if (result != null) {
            viewModel.setId(result.id)
            true
        } else {
            binding.usernameLoginBox.error = "Wrong username"
            Toast.makeText(this.context, "Wrong username", Toast.LENGTH_SHORT).show()
            false
        }

    }
}