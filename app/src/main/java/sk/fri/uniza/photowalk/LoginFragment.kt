package sk.fri.uniza.photowalk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sk.fri.uniza.photowalk.Database.Account
import sk.fri.uniza.photowalk.Database.AppDatabase
import sk.fri.uniza.photowalk.databinding.LoginFragmentBinding


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    private lateinit var binding: LoginFragmentBinding
    private lateinit var database: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout XML file and return a binding object instance
        binding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = AppDatabase.getDatabase(requireActivity().application)

        binding.LoginButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val result = database.accountDao().getAccountId(binding.usernameLoginBox.text.toString(), binding.passwordLoginBox.text.toString())
                if (result != null) {
                    val myIntent = Intent(it.context, MapsActivity::class.java)
                    startActivity(myIntent)
                } else {
                    binding.usernameLoginBox.error = "Wrong username"
                    Toast.makeText(requireActivity().application, "Wrong username", Toast.LENGTH_SHORT).show()
                }

            }


        }

        binding.RegisterButtonLogin.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

}