package sk.fri.uniza.photowalk.Account

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import kotlinx.coroutines.launch
import sk.fri.uniza.photowalk.Database.AppDatabase
import sk.fri.uniza.photowalk.Database.UserData
import sk.fri.uniza.photowalk.R
import sk.fri.uniza.photowalk.Util.Util
import sk.fri.uniza.photowalk.databinding.AccountCreationFragmentBinding
import java.io.*
import java.text.SimpleDateFormat
import java.time.YearMonth
import java.util.*
import kotlin.math.min
import kotlin.math.roundToInt

class AccountCreationFragment : Fragment() {

    private lateinit var binding: AccountCreationFragmentBinding
    private lateinit var database: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.account_creation_fragment, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = AppDatabase.getDatabase(requireActivity().application)
        listCountries()
        setDeaultDate()
        binding.profilePicture.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        }

        binding.confirmButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val model = ViewModelProvider(requireActivity()).get(AccountViewModel::class.java)
                val picture = binding.profilePicture.drawable.toBitmap()
                val byteArray: ByteArray = Util.convertBitmapToByteArray(picture, 300)
                database.userDataDao().addData(
                    UserData(
                        model.id.value!!,
                        binding.profileNameEditText.text.toString(),
                        binding.profileSurnameEditText.text.toString(),
                        binding.country.selectedItem.toString(),
                        binding.day.selectedItem.toString() +
                                ".${binding.month.selectedItem.toString()}" +
                                ".${binding.year.selectedItem.toString()}",
                        byteArray
                        ))
                it.findNavController().navigate(R.id.action_accountCreationFragment_to_loginFragment)
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 100){
            binding.profilePicture.setImageURI(data?.data)
        }
    }

    private fun listCountries() {
        val isoCountryCodes = Locale.getISOCountries()
        val countries = mutableListOf<String>()
        for (countryCode in isoCountryCodes) {
            countries.add(Locale("", countryCode).displayCountry)
        }
        countries.sort()
        binding.country.adapter = ArrayAdapter(requireActivity().application,
            R.layout.spinner_item,
            countries)

    }

    private fun setDeaultDate() {
        val currYear = SimpleDateFormat("yyyy").format(Calendar.getInstance().time).toInt()
        val lastDay: Int = YearMonth.of(currYear, 1).lengthOfMonth()
        val days = mutableListOf<String>()
        for (i in 1..lastDay) days.add(i.toString())
        binding.day.adapter = ArrayAdapter(requireActivity().application,
            R.layout.spinner_item,
            days)

        val years = mutableListOf<String>()
        for (i in currYear downTo currYear-150) years.add(i.toString())
        binding.year.adapter = ArrayAdapter(requireActivity().application,
            R.layout.spinner_item,
            years)
    }
}