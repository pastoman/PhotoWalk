package sk.fri.uniza.photowalk.Gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import sk.fri.uniza.photowalk.Database.AppDatabase
import sk.fri.uniza.photowalk.R
import sk.fri.uniza.photowalk.databinding.FragmentGalleryPreviewBinding


class GalleryPreviewFragment : Fragment(), GalleryRecyclerViewAdapter.OnPictureClickListener {

    private lateinit var binding: FragmentGalleryPreviewBinding
    private lateinit var database: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gallery_preview, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = AppDatabase.getDatabase(requireContext())
        initializeRecyclerView()
    }

    override fun onPictureClick(position: Int) {
        val galleryViewModel = ViewModelProvider(requireActivity())[GalleryViewModel::class.java]
        galleryViewModel.setPicture(galleryViewModel.pictures.value!![position])
        view?.findNavController()?.navigate(R.id.action_galleryPreviewFragment_to_picturePreviewFragment)
    }

    private fun initializeRecyclerView() {
        viewLifecycleOwner.lifecycleScope.launch {
            val galleryViewModel = ViewModelProvider(requireActivity())[GalleryViewModel::class.java]
            binding.galleryRecyclerView.adapter = GalleryRecyclerViewAdapter(
                galleryViewModel.pictures,
                this@GalleryPreviewFragment
            )
            binding.galleryRecyclerView.layoutManager = LinearLayoutManager(requireContext())

            galleryViewModel.pictures.observe(viewLifecycleOwner) {
                binding.galleryRecyclerView.adapter = GalleryRecyclerViewAdapter(
                    galleryViewModel.pictures,
                    this@GalleryPreviewFragment
                )
                binding.galleryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }
}