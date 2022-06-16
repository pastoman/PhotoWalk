package sk.fri.uniza.photowalk.Gallery

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.gms.maps.model.LatLng
import sk.fri.uniza.photowalk.Friends.FriendProfileActivityViewModel
import sk.fri.uniza.photowalk.MainActivity
import sk.fri.uniza.photowalk.R
import sk.fri.uniza.photowalk.databinding.FriendPicturePreviewFragmentBinding

/**
 * Fragment predstavuje nahlad obrazku s moznostou zobrazenia obrazku na mape a s datumom odfotenia
 *
 */
class FriendPicturePreviewFragment : Fragment() {
    private lateinit var binding: FriendPicturePreviewFragmentBinding
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
        binding = DataBindingUtil.inflate(inflater, R.layout.friend_picture_preview_fragment, container, false)

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
        loadData()
        binding.returnButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_friendPicturePreviewFragment_to_friendGalleryPreviewFragment)
        }
        binding.showOnMap.setOnClickListener {
            val mainViewModel = ViewModelProvider(requireActivity())[FriendProfileActivityViewModel::class.java]
            val intent = Intent(it.context, MainActivity::class.java)
            // zdroj: https://stackoverflow.com/questions/6767596/how-to-pass-intent-extras
            val extras = Bundle()
            extras.putInt("id", mainViewModel.mainAccountId.value!!)
            extras.putParcelable("position", LatLng(
                galleryViewModel.picture.value!!.latitude,
                galleryViewModel.picture.value!!.longitude)
            )
            intent.putExtras(extras)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun loadData() {
        binding.pictureView.setImageBitmap(galleryViewModel.picture.value!!.picture)
        binding.date.text = galleryViewModel.picture.value!!.date
    }
}