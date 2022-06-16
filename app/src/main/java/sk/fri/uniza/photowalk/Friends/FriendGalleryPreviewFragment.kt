package sk.fri.uniza.photowalk.Gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import sk.fri.uniza.photowalk.Database.AppDatabase
import sk.fri.uniza.photowalk.R
import sk.fri.uniza.photowalk.databinding.FriendGalleryPreviewFragmentBinding

/**
 * fragment, ktory predstavuje zobrazenie obrazkov v recyclerView
 *
 */
class FriendGalleryPreviewFragment : Fragment(), GalleryRecyclerViewAdapter.OnPictureClickListener {

    private lateinit var binding: FriendGalleryPreviewFragmentBinding
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
        binding = DataBindingUtil.inflate(inflater, R.layout.friend_gallery_preview_fragment, container, false)
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
        initializeRecyclerView()


    }

    /**
     * metoda sa zavola pri kliknuti na obrazok a zabezpeci zobrazenie daneho obrazku na novom
     * fragmente
     *
     * @param position pozicia obrazku v recycler view
     */
    override fun onPictureClick(position: Int) {
        val galleryViewModel = ViewModelProvider(requireActivity())[GalleryViewModel::class.java]
        galleryViewModel.setPicture(galleryViewModel.pictures.value!![position])
        view?.findNavController()?.navigate(R.id.action_friendGalleryPreviewFragment_to_friendPicturePreviewFragment)
    }

    private fun initializeRecyclerView() {
        viewLifecycleOwner.lifecycleScope.launch {
            val galleryViewModel = ViewModelProvider(requireActivity())[GalleryViewModel::class.java]
            galleryViewModel.pictures.observe(viewLifecycleOwner) {
                binding.galleryRecyclerView.adapter = GalleryRecyclerViewAdapter(
                    galleryViewModel.pictures,
                    this@FriendGalleryPreviewFragment
                )
                binding.galleryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }
}