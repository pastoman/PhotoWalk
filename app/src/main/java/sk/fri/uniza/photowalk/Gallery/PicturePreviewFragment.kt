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
import kotlinx.coroutines.launch
import sk.fri.uniza.photowalk.Account.AccountViewModel
import sk.fri.uniza.photowalk.Database.AppDatabase
import sk.fri.uniza.photowalk.Database.UserPictures
import sk.fri.uniza.photowalk.R
import sk.fri.uniza.photowalk.databinding.PicturePreviewFragmentBinding

class PicturePreviewFragment : Fragment() {
    private lateinit var binding: PicturePreviewFragmentBinding
    private lateinit var database: AppDatabase
    private lateinit var galleryViewModel: GalleryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.picture_preview_fragment, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        galleryViewModel = ViewModelProvider(requireActivity())[GalleryViewModel::class.java]
        database = AppDatabase.getDatabase(requireContext())
        loadPicture()
        binding.returnButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_picturePreviewFragment_to_galleryPreviewFragment)
        }

        binding.deleteImage.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                galleryViewModel.removePicture(galleryViewModel.picture.value!!)
                database.userPicturesDao().deletePicture(galleryViewModel.picture.value!!.pictureId)
            }
            it.findNavController().navigate(R.id.action_picturePreviewFragment_to_galleryPreviewFragment)
        }
    }

    private fun loadPicture() {
        binding.pictureView.setImageBitmap(galleryViewModel.picture.value!!.picture)
    }
}