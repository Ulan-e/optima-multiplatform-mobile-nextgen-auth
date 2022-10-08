package kz.optimabank.optima24.room_db.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import kz.optimabank.optima24.db.entry.ForeignBank;

@Dao
public interface ForeignBankDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<ForeignBank> foreignBanks);

    @Query("SELECT * FROM foreignbank")
    List<ForeignBank> getAll();

    @Query("SELECT * FROM foreignbank WHERE id=:id")
    ForeignBank getById(Integer id);

    @Query("DELETE FROM foreignbank")
    void deleteAll();
}
