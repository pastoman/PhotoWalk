package sk.fri.uniza.photowalk.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

/**
 * Interface obsahuje metody pristupu k udajom entity account
 *
 */
@Dao
interface AccountDao {
    /**
     * Pokial ucet s danym menom a heslom existuje v databaze, tak vrati ucet inac vrati null
     *
     * @param username pouzivatelske meno
     * @param password pouzivatelske heslo
     * @return ucet, pokial uz je zaregistrovany
     */
    @Query("SELECT * FROM account WHERE username = :username and password = :password")
    suspend fun checkAccount(username: String, password: String) : Account?

    /**
     * Vrati ucet na zaklade jeho id
     *
     * @param userId id uctu
     * @return ucet, ktory existuje v databaze
     */
    @Query("SELECT * FROM account WHERE id = :userId")
    suspend fun findUsername(userId: Int) : Account?

    /**
     * Vrati ucet priatela na zaklade jeho pouzivatelskeho mena
     *
     * @param username nazov priatela
     * @return ucet priatela
     */
    @Query("SELECT * FROM account WHERE username = :username")
    suspend fun findFriendId(username: String) : Account?

    /**
     * prida pouzivatelsky ucet do databazy
     *
     * @param user pouzivatelsky ucet
     */
    @Insert
    suspend fun addAccount(user: Account)
}

/**
 * Interface obsahuje metody pristupu k udajom entity user_data
 *
 */
@Dao
interface UserDataDao {

    /**
     * ziskanie pouzivatelskych dat
     *
     * @param accountId id pouzivatelskeho uctu
     * @return informacie o pouzivatelovi
     */
    @Query("SELECT * FROM user_data WHERE id = :accountId")
    suspend fun getData(accountId : Int) : UserData?

    /**
     * pridanie pouzivatelskych dat
     *
     * @param data pouzivatelske data
     */
    @Insert(onConflict = REPLACE)
    suspend fun addData(data : UserData)
}

/**
 * Interface obsahuje metody pristupu k udajom entity friend
 *
 */
@Dao
interface FriendDao {

    /**
     * ziskanie vsetkych priatelov daneho uctu
     *
     * @param userId id uctu
     * @return zoznam priatelov uctu
     */
    @Query("SELECT * FROM friend WHERE user_id = :userId")
    suspend fun getAllFriends(userId: Int) : List<Friend>

    /**
     * prida priatela
     *
     * @param friend priatel
     */
    @Insert(onConflict = IGNORE)
    suspend fun addFriend(friend: Friend)

    /**
     * odstrani priatela
     *
     * @param friend priatel
     */
    @Delete
    suspend fun deleteFriend(friend: Friend)
}

/**
 * Interface obsahuje metody pristupu k udajom entity user_pictures
 *
 */
@Dao
interface UserPicturesDao {

    /**
     * ziska vsetky obrazky daneho pouzivatela
     *
     * @param userId id uctu
     * @return zoznam obrazkov viazanych na ucet
     */
    @Query("SELECT * FROM user_pictures WHERE  id_account = :userId")
    suspend fun getAllPictures(userId: Int) : List<UserPictures>

    /**
     * ziska obrazok na zaklade jeho id
     *
     * @param pictureId unikatne id obrazku
     * @return obrazok s danym id
     */
    @Query("SELECT * FROM user_pictures WHERE id_picture = :pictureId")
    suspend fun getPicture(pictureId: Int) : UserPictures

    /**
     * prida obrazok do databazy
     *
     * @param picture obrazok, ktory chceme pridat
     */
    @Insert
    suspend fun addPicture(picture: UserPictures)

    /**
     * vymaze obrazok z databazy na zaklade jeho id
     *
     * @param pictureId id obrazku, ktory chceme vymazat
     */
    @Query("DELETE FROM user_pictures WHERE id_picture = :pictureId")
    suspend fun deletePicture(pictureId: Int)
}