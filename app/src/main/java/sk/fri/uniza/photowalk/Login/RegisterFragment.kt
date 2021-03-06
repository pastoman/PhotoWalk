package sk.fri.uniza.photowalk.Login

import android.database.sqlite.SQLiteConstraintException
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
import sk.fri.uniza.photowalk.Database.Account
import sk.fri.uniza.photowalk.Database.AppDatabase
import sk.fri.uniza.photowalk.R
import sk.fri.uniza.photowalk.databinding.RegisterFragmentBinding
import java.lang.Exception

/**
 * Fragment predstavuje obrazovku registraice a kontroluje, ci je mozne ucet zaregistrovat a
 * po registracii umozni zadat osobne udaje pouzivatela
 *
 */
class RegisterFragment : Fragment() {
    private lateinit var binding: RegisterFragmentBinding
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
        // Inflate the layout XML file and return a binding object instance
        binding = DataBindingUtil.inflate(inflater, R.layout.register_fragment, container, false)

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
        binding.RegisterButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                if (registerAccount()) {
                    it.findNavController().navigate(R.id.action_registerFragment_to_accountCreationFragment)
                }
            }
        }

        binding.LoginButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private suspend fun registerAccount() : Boolean {
        if (binding.passwordRegisterBox.text.toString() != binding.confirmPasswordRegisterBox.text.toString()) {
            binding.passwordRegisterBox.error = "Password and confirm password are not the same"
            binding.confirmPasswordRegisterBox.error = "Password and confirm password are not the same"
            return false
        }
        if(binding.passwordRegisterBox.text.toString().length < 8) {
            binding.passwordRegisterBox.error = "Password must have at lest 8 characters"
            return false
        }

        return try {
            database.accountDao().addAccount(Account(
                0,
                binding.emailRegisterBox.text.toString(),
                binding.usernameRegisterBox.text.toString(),
                binding.passwordRegisterBox.text.toString()
            ))
            val result = database.accountDao()
                .checkAccount(binding.usernameRegisterBox.text.toString(),
                    binding.passwordRegisterBox.text.toString())
            val model = ViewModelProvider(requireActivity()).get(AccountViewModel::class.java)
            model.setId(result!!.id)
            Toast.makeText(this.context, "Account created",
                Toast.LENGTH_LONG).show()
            true
        } catch (e : SQLiteConstraintException) {
            Toast.makeText(this.context, "Username or email is alredy registered",
                Toast.LENGTH_LONG).show()
            false
        } catch (e : Exception) {
            Toast.makeText(this.context, e.message,
                Toast.LENGTH_LONG).show()
            false
        }
    }
}