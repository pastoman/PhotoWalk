package sk.fri.uniza.photowalk.Login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import sk.fri.uniza.photowalk.Database.AppDatabase
import sk.fri.uniza.photowalk.R
import sk.fri.uniza.photowalk.databinding.AccountCreationFragmentBinding
import java.text.SimpleDateFormat
import java.time.YearMonth
import java.util.*

class AccountCreationFragment : Fragment() {

    private lateinit var binding: AccountCreationFragmentBinding
    private lateinit var database: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout XML file and return a binding object instance
        binding = DataBindingUtil.inflate(inflater, R.layout.account_creation_fragment, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = AppDatabase.getDatabase(requireActivity().application)

        val currYear =  Calendar.getInstance().time
        val df = SimpleDateFormat("yyyy")
        val year = df.format(currYear)
        val lastDay: Int = YearMonth.of(year.toInt(), 1).lengthOfMonth()
        val days = mutableListOf<String>()
        for (i in 1..lastDay) days.add(i.toString())
        binding.day.adapter = ArrayAdapter(requireActivity().application, R.layout.spinner_item, days)

        val years = mutableListOf<String>()

        for (i in year.toInt()-150..year.toInt()) years.add(i.toString())
        binding.year.adapter = ArrayAdapter(requireActivity().application,
            R.layout.spinner_item,
            years)
        binding.year.setSelection(binding.year.adapter.count-1)
    }
}