package sk.fri.uniza.photowalk.Gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

import androidx.lifecycle.LiveData

import androidx.recyclerview.widget.RecyclerView
import sk.fri.uniza.photowalk.R

/**
 * adapter pre recyclerView, ktory urcuje, ako manipulovat s jednotlivymi polozkami
 *
 * @property pictures LiveData, ktore obsahuju zoznam instancii datovej triedy Picture
 * @property onPictureClickListener listener, ktory sa zavola pri stlaceni na obrazok
 */
class GalleryRecyclerViewAdapter(private val pictures : LiveData<List<Picture>>,
                                 private val onPictureClickListener: OnPictureClickListener) :
    RecyclerView.Adapter<GalleryRecyclerViewAdapter.GalleryViewHolder>() {

    /**
     * metoda zabezpecuje vytvorenie holdera typu GalleryViewHolder
     *
     * @param parent kontext otca
     * @param viewType typ pohladu
     * @return holder typu GalleryViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.gallery_item,
            parent, false)
        return GalleryViewHolder(itemView, onPictureClickListener)
    }

    /**
     * sluzi na inicializaciu prvkov holdera z atributu friendsList
     *
     * @param holder holder typu GalleryViewHolder
     * @param position index prvku holdera
     */
    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val currentItem = pictures.value!![position]

        holder.picture.setImageBitmap(currentItem.picture)

    }

    /**
     * vrati pocet prvkov v holderi
     *
     * @return pocet prvkov v holderi
     */
    override fun getItemCount(): Int {
        return pictures.value!!.size
    }

    /**
     * Holder pre recyclerView, ktory obsahuje prvky layoutu gallery_item.xml
     *
     * @property onPictureClickListener listener, ktory sa zavola pri stlaceni na obrazok
     * @constructor
     * inicializuje vsetky atributy a listener
     *
     * @param itemView pohlad daneho prvku
     */
    class GalleryViewHolder(itemView: View, private val onPictureClickListener : OnPictureClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val picture : ImageButton = itemView.findViewById(R.id.gallery_picture)

        init {
            picture.setOnClickListener(this)
        }
        override fun onClick(view: View) {
            onPictureClickListener.onPictureClick(adapterPosition)
        }
    }

    /**
     * listener, ktory pocuva na kliknutie na obrazok
     *
     */
    interface OnPictureClickListener {
        /**
         * metoda sa zavola po kliknuti na obrazok
         *
         * @param position index prvku holdera
         */
        fun onPictureClick(position: Int)
    }
}