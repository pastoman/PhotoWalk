package sk.fri.uniza.photowalk.Gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import sk.fri.uniza.photowalk.Database.AppDatabase
import sk.fri.uniza.photowalk.Friends.MainActivityViewModel
import sk.fri.uniza.photowalk.Map.MapsFragment
import sk.fri.uniza.photowalk.R
import sk.fri.uniza.photowalk.databinding.PicturePreviewFragmentBinding

/**
 * Fragment predstavuje nahlad obrazku s moznostou zobrazenia obrazku na mape a s datumom odfotenia
 *
 */
class PicturePreviewFragment : Fragment() {
    private lateinit var binding: PicturePreviewFragmentBinding
    private lateinit var database: AppDatabase
    private lateinit var galleryViewModel: GalleryViewModel

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
        binding = DataBindingUtil.inflate(inflater, R.layout.picture_preview_fragment,
            container, false)

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
        galleryViewModel = ViewModelProvider(requireActivity())[GalleryViewModel::class.java]
        database = AppDatabase.getDatabase(requireContext())
        loadData()
        initializeReturnButtonListener()
        initializeDeleteButtonListener()
        initializeShowOnMapListener()

    }

    private fun loadData() {
        binding.pictureView.setImageBitmap(galleryViewModel.picture.value!!.picture)
        binding.date.text = galleryViewModel.picture.value!!.date
    }

    private fun initializeShowOnMapListener() {
        binding.showOnMap.setOnClickListener {
            val mainViewModel =
                ViewModelProvider(requireActivity())[MainActivityViewModel::class.java]
            mainViewModel.setPosition(
                LatLng(
                    galleryViewModel.picture.value!!.latitude,
                    galleryViewModel.picture.value!!.longitude
                )
            )
            if (galleryViewModel.fromMap.value!!) {
                requireActivity().supportFragmentManager.popBackStack()
            } else {
                val ft: FragmentTransaction =
                    requireActivity().supportFragmentManager.beginTransaction()
                ft.replace(R.id.mainFragment, MapsFragment())
                ft.commit()
            }
        }
    }

    private fun initializeReturnButtonListener() {
        binding.returnButton.setOnClickListener {
            if (galleryViewModel.fromMap.value!!) {
                requireActivity().supportFragmentManager.popBackStack()
            } else {
                it.findNavController()
                    .navigate(R.id.action_picturePreviewFragment_to_galleryPreviewFragment)
            }
        }
    }

    private fun initializeDeleteButtonListener() {
        if (galleryViewModel.editable.value!!)
        {
            binding.deleteImage.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    galleryViewModel.removePicture(galleryViewModel.picture.value!!)
                    database.userPicturesDao().deletePicture(galleryViewModel.picture.value!!.pictureId)
                    if (galleryViewModel.fromMap.value!!) {
                        requireActivity().supportFragmentManager.popBackStack()
                    } else {
                        it.findNavController()
                            .navigate(R.id.action_picturePreviewFragment_to_galleryPreviewFragment)
                    }
                }
            }
        } else {
            binding.deleteImage.isVisible = false
        }
    }
}