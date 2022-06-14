package sk.fri.uniza.photowalk.Gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

import androidx.lifecycle.LiveData

import androidx.recyclerview.widget.RecyclerView
import sk.fri.uniza.photowalk.R

class GalleryRecyclerViewAdapter(private val pictures : LiveData<List<Picture>>,
                                 private val onPictureClickListener: OnPictureClickListener) :
    RecyclerView.Adapter<GalleryRecyclerViewAdapter.GalleryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.gallery_item,
            parent, false)
        return GalleryViewHolder(itemView, onPictureClickListener)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val currentItem = pictures.value!![position]

        holder.picture.setImageBitmap(currentItem.picture)

    }

    override fun getItemCount(): Int {
        return pictures.value!!.size
    }

    class GalleryViewHolder(itemView: View, private val onPictureClickListener : OnPictureClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val picture : ImageButton = itemView.findViewById(R.id.gallery_picture)

        init {
            picture.setOnClickListener(this)
        }
        override fun onClick(view: View) {
            onPictureClickListener.onPictureClick(adapterPosition)
        }
    }

    interface OnPictureClickListener {
        fun onPictureClick(position: Int)
    }
}