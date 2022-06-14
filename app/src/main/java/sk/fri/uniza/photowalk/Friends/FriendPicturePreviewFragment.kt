package sk.fri.uniza.photowalk.Gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import sk.fri.uniza.photowalk.R
import sk.fri.uniza.photowalk.databinding.FriendPicturePreviewFragmentBinding

class FriendPicturePreviewFragment : Fragment() {
    private lateinit var binding: FriendPicturePreviewFragmentBinding
    private lateinit var galleryViewModel: GalleryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.friend_picture_preview_fragment, container, false)
        galleryViewModel = ViewModelProvider(requireActivity())[GalleryViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadPicture()
        binding.returnButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_friendPicturePreviewFragment_to_friendGalleryPreviewFragment)
        }
    }

    private fun loadPicture() {
        binding.pictureView.setImageBitmap(galleryViewModel.picture.value!!.picture)
    }
}