package sk.fri.uniza.photowalk.Friends

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import sk.fri.uniza.photowalk.Account.AccountViewModel
import sk.fri.uniza.photowalk.Database.AppDatabase
import sk.fri.uniza.photowalk.Gallery.GalleryViewModel
import sk.fri.uniza.photowalk.Gallery.Picture
import sk.fri.uniza.photowalk.R
import sk.fri.uniza.photowalk.Util.Util
import sk.fri.uniza.photowalk.databinding.FriendGalleryFragmentBinding
import sk.fri.uniza.photowalk.databinding.GalleryFragmentBinding

class FriendGalleryFragment : Fragment() {

    private lateinit var binding: FriendGalleryFragmentBinding
    private lateinit var database: AppDatabase
    private lateinit var viewModel: GalleryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.friend_gallery_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = AppDatabase.getDatabase(requireContext())
        viewModel = ViewModelProvider(requireActivity())[GalleryViewModel::class.java]
        initializeViewModel()
        val friendViewModel = ViewModelProvider(requireActivity())[FriendProfileActivityViewModel::class.java]
        friendViewModel.setTabIndex(TAB_INDEX)
    }

    private fun initializeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.setFromMap(false)
            viewModel.setEditable(false)
            viewModel.clearPictures()
            val model = ViewModelProvider(requireActivity())[AccountViewModel::class.java]
            val result = database.userPicturesDao().getAllPictures(model.id.value!!)
            if (result.isNotEmpty()) {
                for (item in result) {
                    viewModel.addPicture(
                        Picture(
                            item.id_picture,
                            Util.convertByteArrayToBitmap(item.picture),
                            item.latitude,
                            item.longitude,
                            Util.StringToDate(item.date)
                        )
                    )
                }
            }
        }
    }

    companion object {
        private const val TAB_INDEX = 0
    }

}