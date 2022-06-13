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
    @PrimaryKey(autoGenerate = true) val id : Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "surname") val surname: String?,
    @ColumnInfo(name = "country") val country: String?,
    @ColumnInfo(name = "birthday") val birthday: String?,
    @ColumnInfo(name = "profile_picture", typeAffinity = ColumnInfo.BLOB) val picture: ByteArray?
)
