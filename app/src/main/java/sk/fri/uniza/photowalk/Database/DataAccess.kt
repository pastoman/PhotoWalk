package sk.fri.uniza.photowalk.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface AccountDao {
    @Query("SELECT * FROM account WHERE username = :username and password = :password")
    suspend fun getAccountId(username: String, password: String) : Account?

    @Query("SELECT * FROM account WHERE id = :userId")
    suspend fun findUsername(userId: Int) : Account?

    @Query("SELECT * FROM account WHERE username = :username")
    suspend fun findFriendId(username: String) : Account?

    @Insert
    suspend fun addAccount(user: Account)
}

@Dao
interface UserDataDao {

    @Query("SELECT * FROM user_data WHERE id = :accountId")
    suspend fun getData(accountId : Int) : List<UserData>

    @Insert(onConflict = REPLACE)
    suspend fun addData(data : UserData)
}

@Dao
interface FriendDao {

    @Query("SELECT * FROM friend WHERE user_id = :userId")
    suspend fun getAllFriends(userId: Int) : List<Friend>

    @Insert(onConflict = IGNORE)
    suspend fun addFriend(friend: Friend)

    @Delete
    suspend fun deleteFriend(friend: Friend)
}

@Dao
interface UserPicturesDao {

    @Query("SELECT * FROM user_pictures WHERE  id_account = :userId")
    suspend fun getAllPictures(userId: Int) : List<UserPictures>

    @Insert
    suspend fun addPicture(picture: UserPictures)

    @Delete
    suspend fun deletePicture(picture: UserPictures)
}