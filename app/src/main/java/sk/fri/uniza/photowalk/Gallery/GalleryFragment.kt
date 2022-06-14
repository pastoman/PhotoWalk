package sk.fri.uniza.photowalk.Gallery

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import sk.fri.uniza.photowalk.Account.AccountViewModel
import sk.fri.uniza.photowalk.Database.AppDatabase
import sk.fri.uniza.photowalk.R
import sk.fri.uniza.photowalk.Util.Util
import sk.fri.uniza.photowalk.databinding.GalleryFragmentBinding


class GalleryFragment : Fragment(), GalleryRecyclerViewAdapter.OnPictureClickListener {

    private lateinit var binding: GalleryFragmentBinding
    private val viewModel: GalleryViewModel by viewModels()
    private lateinit var database: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.gallery_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = AppDatabase.getDatabase(requireContext())
        initializeRecyclerView()


    }

    override fun onPictureClick(position: Int) {
        val builder = Dialog(requireContext())
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
        builder.window?.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        builder.setOnDismissListener(DialogInterface.OnDismissListener {
            //nothing
        })

        val imageView = ImageView(requireContext())
        imageView.setImageBitmap(viewModel.pictures.value!![position].picture)
        builder.addContentView(
            imageView, RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        builder.show()
    }

       private fun initializeRecyclerView() {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.clear()
                val model = ViewModelProvider(requireActivity()).get(AccountViewModel::class.java)
                val result = database.userPicturesDao().getAllPictures(model.id.value!!)
                if (result.isNotEmpty()) {
                    for (item in result) {
                        viewModel.addPicture(
                            Picture(
                                Util.convertByteArrayToBitmap(item.picture),
                                item.latitude, item.longitude, Util.StringToDate(item.date)
                            )
                        )
                    }
                }
                binding.galleryRecyclerView.adapter = GalleryRecyclerViewAdapter(
                    viewModel.pictures,
                    this@GalleryFragment
                )
                binding.galleryRecyclerView.layoutManager = LinearLayoutManager(requireContext())

                viewModel.pictures.observe(viewLifecycleOwner) {
                    binding.galleryRecyclerView.adapter = GalleryRecyclerViewAdapter(
                        viewModel.pictures,
                        this@GalleryFragment
                    )
                    binding.galleryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                }
        }
    }
}