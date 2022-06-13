package sk.fri.uniza.photowalk.Friends

import android.content.Intent
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

class FriendsRecyclerViewAdapter(private val friendsList: List<Friend>, private val userId : Int, private val friendsListFragment: FriendsListFragment) : RecyclerView.Adapter<FriendsRecyclerViewAdapter.FriendsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.friend,
            parent, false)
        return FriendsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        val currentItem = friendsList[position]

        holder.friendName.text = currentItem.name
        holder.placesButton.setOnClickListener {
            val intent = Intent(it.context, FriendProfileActivity::class.java)
            intent.putExtra("id", currentItem.id)
            startActivity(it.context, intent, null)
        }
        holder.removeFriendButton.setOnClickListener {
            it.findViewTreeLifecycleOwner()!!.lifecycleScope.launch {
                AppDatabase.getDatabase(it.context).friendDao().deleteFriend(
                    sk.fri.uniza.photowalk.Database.Friend(userId, currentItem.id))
                friendsListFragment.loadFriends()
            }

        }
    }

    override fun getItemCount(): Int {
        return friendsList.size
    }

    class FriendsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val friendName : TextView = itemView.findViewById(R.id.friendName)
            val placesButton : ImageButton = itemView.findViewById(R.id.placesButton)
            val removeFriendButton : ImageButton = itemView.findViewById(R.id.removeFriendButton)

        }

}