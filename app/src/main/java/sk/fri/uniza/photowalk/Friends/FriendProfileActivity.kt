package sk.fri.uniza.photowalk.Friends

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.tabs.TabLayout
import sk.fri.uniza.photowalk.Account.AccountFragment
import sk.fri.uniza.photowalk.Account.AccountViewModel
import sk.fri.uniza.photowalk.MainActivity
import sk.fri.uniza.photowalk.Map.MapsFragment
import sk.fri.uniza.photowalk.R
import sk.fri.uniza.photowalk.databinding.ActivityFriendProfileBinding

class FriendProfileActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {
    private lateinit var binding : ActivityFriendProfileBinding
    private lateinit var viewModel: AccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_friend_profile
        )
        viewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        viewModel.setId(intent.getIntExtra("id",0))

        val friendViewModel = ViewModelProvider(this)[FriendProfileActivityViewModel::class.java]
        friendViewModel.setMainAccountId(intent.getIntExtra("accountId",0))
        friendViewModel.tabIndex.observe(this) {
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(it))
        }
        binding.tabLayout.addOnTabSelectedListener(this)
        supportActionBar?.hide()

    }

    override fun onBackPressed() {
        val mainViewModel = ViewModelProvider(this)[FriendProfileActivityViewModel::class.java]
        val intent = Intent(application, MainActivity::class.java)
        val extras = Bundle()
        extras.putInt("id", mainViewModel.mainAccountId.value!!)
        extras.putParcelable("position", null)
        intent.putExtras(extras)
        startActivity(intent)
        finish()
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        if (tab != null) {
            val fragment : Fragment = when (tab.position) {
                0 -> FriendGalleryFragment()
                else -> FriendProfilePreviewFragment()
            }
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(R.id.mainFriendFragment, fragment)
            ft.addToBackStack(null)
            ft.commit()
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }
}