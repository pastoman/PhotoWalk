package sk.fri.uniza.photowalk.Friends

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import sk.fri.uniza.photowalk.Database.AppDatabase
import sk.fri.uniza.photowalk.R

/**
 * adapter pre recyclerView, ktory urcuje, ako manipulovat s jednotlivymi polozkami
 *
 * @property friendsList zoznam priatelov, ktorych chceme pridat do recyclerView
 * @property userId id priatela
 * @property friendsListFragment odkaz na fragment, v ktorom je recyclerView pouzivany
 */
class FriendsRecyclerViewAdapter(
    private val friendsList: List<FriendData>,
    private val userId : Int,
    private val friendsListFragment: FriendsListFragment)
        : RecyclerView.Adapter<FriendsRecyclerViewAdapter.FriendsViewHolder>() {

    /**
     * metoda zabezpecuje vytvorenie holdera typu FriendsViewHolder
     *
     * @param parent kontext otca
     * @param viewType typ pohladu
     * @return holder typu FriendsViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.friend,
            parent, false)
        return FriendsViewHolder(itemView)
    }

    /**
     * sluzi na inicializaciu prvkov holdera z atributu friendsList
     *
     * @param holder holder typu FriendsViewHolder
     * @param position index prvku holdera
     */
    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        val currentItem = friendsList[position]

        holder.friendName.text = currentItem.name
        holder.placesButton.setOnClickListener {
            val intent = Intent(it.context, FriendProfileActivity::class.java)
            val extras: Bundle = Bundle()
            extras.putInt("id", currentItem.id)
            extras.putInt("accountId", userId)
            intent.putExtras(extras)
            startActivity(it.context, intent, null)
            friendsListFragment.requireActivity().finish()
        }
        holder.removeFriendButton.setOnClickListener {
            it.findViewTreeLifecycleOwner()!!.lifecycleScope.launch {
                AppDatabase.getDatabase(it.context).friendDao().deleteFriend(
                    sk.fri.uniza.photowalk.Database.Friend(userId, currentItem.id))
                friendsListFragment.loadFriends()
            }

        }
    }

    /**
     * vrati pocet prvkov v holderi
     *
     * @return pocet prvkov v holderi
     */
    override fun getItemCount(): Int {
        return friendsList.size
    }

    /**
     * Holder pre recyclerView, ktory obsahuje prvky layoutu friend.xml
     *
     * @constructor
     * inicializuje vsetky atributy
     *
     * @param itemView pohlad daneho prvku
     */
    class FriendsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val friendName : TextView = itemView.findViewById(R.id.friendName)
            val placesButton : ImageButton = itemView.findViewById(R.id.placesButton)
            val removeFriendButton : ImageButton = itemView.findViewById(R.id.removeFriendButton)

        }

}