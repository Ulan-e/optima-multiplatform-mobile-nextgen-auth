package kz.optimabank.optima24.local.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import kz.optimabank.optima24.db.entry.ProfilePicture;

@Dao
public interface ProfilePictureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(ProfilePicture profilePicture);

    @Query("SELECT * FROM profilepicture WHERE phone=:phone")
    ProfilePicture getByPhone(String phone);

    @Query("DELETE FROM profilepicture")
    void delete();
}
