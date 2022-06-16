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

/**
 * Hlavna aktivita, ktora sa spusta pri prehliadani uctu priatela
 *
 */
class FriendProfileActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {
    private lateinit var binding : ActivityFriendProfileBinding
    private lateinit var viewModel: AccountViewModel

    /**
     * metoda sa zavola pri vytvoreni aktivity, sluzi na inicializaciu tried potrebnych na
     * fungovanie aktivity
     *
     * @param savedInstanceState ulozeny predchadzajuci stav pri behu aplikacie
     */
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

    /**
     * metoda sa zavola pri stlaceni tlacidla spat a sluzi na ukoncenie tejto aktivity a navratu
     * do hlavnej aktivity uctu
     *
     */
    override fun onBackPressed() {
        val mainViewModel = ViewModelProvider(this)[FriendProfileActivityViewModel::class.java]
        val intent = Intent(application, MainActivity::class.java)
        // zdroj: https://stackoverflow.com/questions/6767596/how-to-pass-intent-extras
        val extras = Bundle()
        extras.putInt("id", mainViewModel.mainAccountId.value!!)
        extras.putParcelable("position", null)
        intent.putExtras(extras)
        startActivity(intent)
        finish()
    }

    /**
     * metoda tabLayout, ktora sa zavola pri zakliknuti zalozky
     *
     * @param tab zalozka, ktora bola nakliknuta
     */
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

    /**
     * metoda sa vola pri zruseni vyberu zalozky
     *
     * @param tab zalozka
     */
    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    /**
     * metoda sa vola pri opatovnom kliknuti na zalozku
     *
     * @param tab zalozka
     */
    override fun onTabReselected(tab: TabLayout.Tab?) {
    }
}