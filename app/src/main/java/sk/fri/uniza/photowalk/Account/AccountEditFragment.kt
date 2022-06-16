package sk.fri.uniza.photowalk.Account

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
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
import sk.fri.uniza.photowalk.databinding.AccountEditFragmentBinding
import java.text.SimpleDateFormat
import java.time.YearMonth
import java.util.*

/**
 * Fragment, ktory sluzi na upravu dodatocnych informacii pri prehliadani prihlaseneho uctu
 *
 */
class AccountEditFragment : Fragment() {
    private lateinit var binding: AccountEditFragmentBinding
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
        binding = DataBindingUtil.inflate(inflater, R.layout.account_edit_fragment, container, false)

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
        listCountries()
        setDefaultDate()
        loadInfo()
        binding.profilePicture.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        }

        binding.confirmButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val model = ViewModelProvider(requireActivity()).get(AccountViewModel::class.java)
                val picture = binding.profilePicture.drawable.toBitmap()
                val byteArray: ByteArray = Util.convertBitmapToByteArray(picture,1200)
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
                it.findNavController().navigate(R.id.action_accountEditFragment_to_accountInfoFragment)
            }

        }

    }

    /**
     * Vyhodnotenie vysledku systemovych poziadaviek a intentov
     *
     * @param requestCode specificky kod nasej poziadavky
     * @param resultCode kod vysledku spracovania poziadavky
     * @param data data, ktore vratila poziadavka
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 100){
            binding.profilePicture.setImageURI(data?.data)
        }
    }

    private fun loadInfo() {
        viewLifecycleOwner.lifecycleScope.launch  {
            try {
                val model = ViewModelProvider(requireActivity()).get(AccountViewModel::class.java)
                val result = database.userDataDao().getData(model.id.value!!)
                val picture = Util.convertByteArrayToBitmap(result!!.picture!!)
                binding.profilePicture.setImageBitmap(picture)
                binding.profileNameEditText.setText(result.name)
                binding.profileSurnameEditText.setText(result.surname)
                val birthday = result.birthday
                binding.day.setSelection(birthday!!.split(".").toTypedArray()[0].toInt()-1)
                for (i in 0 until binding.month.count) {
                    if (binding.month.getItemAtPosition(i).toString() == birthday.split(".").toTypedArray()[1]) {
                        binding.month.setSelection(i)
                        break
                    }
                }
                val currYear = SimpleDateFormat("yyyy").format(Calendar.getInstance().time).toInt()
                val year = birthday.split(".").toTypedArray()[2].toInt()
                val newYear = (currYear - year)
                if (newYear < 150) {
                    binding.year.setSelection(newYear)
                }
                for (i in 0 until binding.country.count) {
                    if (binding.country.getItemAtPosition(i).toString() == result.country) {
                        binding.country.setSelection(i)
                        break
                    }
                }
            } catch (e : Exception) {
                Toast.makeText(requireContext(), e.message,
                    Toast.LENGTH_LONG).show()
            }
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

    private fun setDefaultDate() {
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