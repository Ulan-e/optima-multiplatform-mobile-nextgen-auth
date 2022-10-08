package kz.optimabank.optima24.room_db.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import kz.optimabank.optima24.db.entry.Country;

@Dao
public interface CountryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<Country> countries);

    @Query("SELECT * FROM country")
    List<Country> getAll();

    @Query("DELETE FROM country")
    void deleteAll();
}