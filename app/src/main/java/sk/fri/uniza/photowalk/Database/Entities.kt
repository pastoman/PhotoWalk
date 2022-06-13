package sk.fri.uniza.photowalk.Database

import android.graphics.Bitmap
import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(tableName = "account" ,
    indices = [Index(value = ["email"], unique = true), Index(value = ["username"], unique = true)])
data class Account(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "password") val password: String
)

@Entity(tableName = "user_data")
data class UserData(
    @PrimaryKey val id : Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "surname") val surname: String?,
    @ColumnInfo(name = "country") val country: String?,
    @ColumnInfo(name = "birthday") val birthday: String?,
    @ColumnInfo(name = "profile_picture", typeAffinity = ColumnInfo.BLOB) val picture: ByteArray?
)

@Entity(tableName = "friend", primaryKeys = ["user_id", "friend_id"])
data class Friend(
    @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "friend_id") val friendId: Int
)
