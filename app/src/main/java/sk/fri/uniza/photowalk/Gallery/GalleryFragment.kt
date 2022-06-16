package sk.fri.uniza.photowalk.Gallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import sk.fri.uniza.photowalk.Account.AccountViewModel
import sk.fri.uniza.photowalk.Database.AppDatabase
import sk.fri.uniza.photowalk.Friends.MainActivityViewModel
import sk.fri.uniza.photowalk.R
import sk.fri.uniza.photowalk.Util.Util
import sk.fri.uniza.photowalk.databinding.GalleryFragmentBinding

/**
 *  Fragment, ktory predstavuje NavHostFragment pre navigation_gallery.xml
 *
 */
class GalleryFragment : Fragment() {

    private lateinit var binding: GalleryFragmentBinding
    private lateinit var database: AppDatabase
    private lateinit var viewModel: GalleryViewModel

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
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.gallery_fragment, container, false)
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
        database = AppDatabase.getDatabase(requireContext())
        viewModel = ViewModelProvider(requireActivity())[GalleryViewModel::class.java]
        val mainViewModel =ViewModelProvider(requireActivity())[MainActivityViewModel::class.java]
        mainViewModel.setTabIndex(TAB_INDEX)
    }


    companion object {
        private const val TAB_INDEX = 1
    }

}