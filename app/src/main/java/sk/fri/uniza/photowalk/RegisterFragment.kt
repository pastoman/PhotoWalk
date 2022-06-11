package sk.fri.uniza.photowalk

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import sk.fri.uniza.photowalk.databinding.LoginFragmentBinding
import sk.fri.uniza.photowalk.databinding.RegisterFragmentBinding


class RegisterFragment : Fragment() {
    private lateinit var binding: RegisterFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout XML file and return a binding object instance
        binding = DataBindingUtil.inflate(inflater, R.layout.register_fragment, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.RegisterButton.setOnClickListener {
            val myIntent = Intent(it.context, MapsActivity::class.java)
            startActivity(myIntent)
        }

        binding.LoginButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

}