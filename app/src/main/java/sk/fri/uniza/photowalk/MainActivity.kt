package sk.fri.uniza.photowalk

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.coroutines.launch
import sk.fri.uniza.photowalk.Account.AccountFragment
import sk.fri.uniza.photowalk.Account.AccountViewModel
import sk.fri.uniza.photowalk.Database.AppDatabase
import sk.fri.uniza.photowalk.Friends.FriendsListFragment
import sk.fri.uniza.photowalk.Friends.MainActivityViewModel
import sk.fri.uniza.photowalk.Gallery.GalleryFragment
import sk.fri.uniza.photowalk.Gallery.GalleryPreviewFragment
import sk.fri.uniza.photowalk.Gallery.GalleryViewModel
import sk.fri.uniza.photowalk.Gallery.Picture
import sk.fri.uniza.photowalk.Map.MapsFragment
import sk.fri.uniza.photowalk.Util.Util
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
        viewModel.setPosition(intent.getParcelableExtra("position"))

        viewModel.tabIndex.observe(this) {
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(it))
        }

        binding.tabLayout.addOnTabSelectedListener(this)
        supportActionBar?.hide()
        initializeGalleryViewModel()

    }

    private fun initializeGalleryViewModel() {
        lifecycleScope.launch {
            val viewModel = ViewModelProvider(this@MainActivity)[GalleryViewModel::class.java]
            val database = AppDatabase.getDatabase(this@MainActivity)
            viewModel.setFromMap(false)
            viewModel.setEditable(true)
            viewModel.clearPictures()
            val model = ViewModelProvider(this@MainActivity)[AccountViewModel::class.java]
            val result = database.userPicturesDao().getAllPictures(model.id.value!!)
            if (result.isNotEmpty()) {
                for (item in result) {
                    viewModel.addPicture(
                        Picture(
                            item.id_picture,
                            Util.convertByteArrayToBitmap(item.picture),
                            item.latitude,
                            item.longitude,
                            item.date
                        )
                    )
                }
            }
        }
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
            ft.commit()
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }
}