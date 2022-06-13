package sk.fri.uniza.photowalk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import sk.fri.uniza.photowalk.Account.AccountFragment
import sk.fri.uniza.photowalk.Account.AccountViewModel
import sk.fri.uniza.photowalk.Friends.FriendsListFragment
import sk.fri.uniza.photowalk.Map.MapsFragment
import sk.fri.uniza.photowalk.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), OnTabSelectedListener {
    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModel: AccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityMainBinding>(
            this,
            R.layout.activity_main
        )
        viewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        viewModel.setId(intent.getIntExtra("id",0))
        binding.tabLayout.addOnTabSelectedListener(this)
        supportActionBar?.hide()

    }

    override fun onBackPressed() {
        moveTaskToBack(true)
        android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(0)
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        if (tab != null) {
            val fragment : Fragment = when (tab.position) {
                0 -> MapsFragment()
                2 -> FriendsListFragment()
                else -> AccountFragment()
            }
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(R.id.mainFragment, fragment)
            ft.addToBackStack(null)
            ft.commit()
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }
}