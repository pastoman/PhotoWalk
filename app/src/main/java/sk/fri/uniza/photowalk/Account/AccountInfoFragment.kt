package sk.fri.uniza.photowalk.Account

import android.R.attr.bitmap
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
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
import sk.fri.uniza.photowalk.Map.MapsActivity
import sk.fri.uniza.photowalk.R
import sk.fri.uniza.photowalk.databinding.AccountInfoFragmentBinding


class AccountInfoFragment : Fragment() {

    lateinit var binding: AccountInfoFragmentBinding
    lateinit var database: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.account_info_fragment, container, false)

        database = AppDatabase.getDatabase(requireContext())

        return binding.root
    }

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
                val picture = convertByteArrayToBitmap(result[0].picture!!)
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

    @Suppress("DEPRECATION")
    private fun convertByteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

}