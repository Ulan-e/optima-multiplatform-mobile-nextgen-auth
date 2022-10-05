package kz.optimabank.optima24.room_db.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import kz.optimabank.optima24.db.entry.Dictionary;

@Dao
public interface DictionaryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertAll(List<Dictionary> dictionaries);

    @Query("SELECT * FROM dictionary")
    List<Dictionary> getAll();

    @Query("SELECT * FROM dictionary WHERE type=:type")
    List<Dictionary> fetchByType(Integer type);

    @Query("SELECT * FROM dictionary WHERE code=:code")
    Dictionary getByCode(String code);

    @Query("DELETE FROM notification")
    Completable deleteAll();
}