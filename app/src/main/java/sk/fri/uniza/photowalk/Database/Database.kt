package sk.fri.uniza.photowalk.Database

import android.content.Context
import android.graphics.Bitmap
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Aplikacna databaza, ktora obsahuje entity account, user_data, friend a user_pictures
 *
 */
@Database(entities = [Account::class, UserData::class, Friend::class, UserPictures::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    /**
     * vrati object pre pristup k entite account
     *
     * @return objekt pre pristup k datam entity account
     */
    abstract fun accountDao(): AccountDao

    /**
     * vrati object pre pristup k entite user_data
     *
     * @return objekt pre pristup k datam entity user_data
     */
    abstract fun userDataDao(): UserDataDao

    /**
     * vrati object pre pristup k entite friend
     *
     * @return objekt pre pristup k datam entity friend
     */
    abstract fun friendDao(): FriendDao

    /**
     * vrati object pre pristup k entite user_pictures
     *
     * @return objekt pre pristup k datam entity user_pictures
     */
    abstract fun userPicturesDao() : UserPicturesDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * singleton triedy AppDatabase
         *
         * @param context kontext databazy
         * @return instancia triedy AppDatabase
         */
        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}