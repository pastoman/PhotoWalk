package sk.fri.uniza.photowalk.Friends

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import sk.fri.uniza.photowalk.Account.AccountFragment
import sk.fri.uniza.photowalk.Account.AccountViewModel
import sk.fri.uniza.photowalk.Map.MapsFragment
import sk.fri.uniza.photowalk.R
import sk.fri.uniza.photowalk.databinding.ActivityFriendProfileBinding

class FriendProfileActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {
    private lateinit var binding : ActivityFriendProfileBinding
    private lateinit var viewModel: AccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityFriendProfileBinding>(
            this,
            R.layout.activity_friend_profile
        )
        viewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        viewModel.setId(intent.getIntExtra("id",0))
        binding.tabLayout.addOnTabSelectedListener(this)
        supportActionBar?.hide()

    }

    override fun onBackPressed() {
        finish()
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        if (tab != null) {
            val fragment : Fragment = when (tab.position) {
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