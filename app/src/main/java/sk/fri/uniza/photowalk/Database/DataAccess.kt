package sk.fri.uniza.photowalk.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AccountDao {
    @Query("SELECT * FROM account WHERE username = :username and password = :password")
    suspend fun getAccountId(username: String, password: String) : Account?

    @Insert
    suspend fun addAccount(user: Account)
}

@Dao
interface UserDataDao {

    @Query("SELECT * FROM user_data WHERE id = :accountId")
    suspend fun getData(accountId : Int) : List<UserData>

    @Insert
    suspend fun addData(data : UserData)
}