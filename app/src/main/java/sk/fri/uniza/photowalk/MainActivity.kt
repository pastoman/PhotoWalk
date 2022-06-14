package sk.fri.uniza.photowalk

import android.os.Bundle
import androidx.activity.viewModels
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
import sk.fri.uniza.photowalk.Friends.MainActivityViewModel
import sk.fri.uniza.photowalk.Gallery.GalleryFragment
import sk.fri.uniza.photowalk.Gallery.GalleryPreviewFragment
import sk.fri.uniza.photowalk.Map.MapsFragment
import sk.fri.uniza.photowalk.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), OnTabSelectedListener {
    private lateinit var binding : ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityMainBinding>(
            this,
            R.layout.activity_main
        )
        val accountModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        accountModel.setId(intent.getIntExtra("id",0))

        binding.tabLayout.selectTab(binding.tabLayout.getTabAt(viewModel.tabIndex.value!!))
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
            viewModel.setTabIndex(tab.position)
            val fragment : Fragment = when (tab.position) {
                0 -> MapsFragment()
                1 -> GalleryFragment()
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