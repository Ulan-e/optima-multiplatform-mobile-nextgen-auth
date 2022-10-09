package kz.optimabank.optima24.local.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import kz.optimabank.optima24.db.entry.PaymentRegions;

@Dao
public interface PaymentRegionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<PaymentRegions> paymentRegions);

    @Query("SELECT * FROM paymentregions")
    List<PaymentRegions> getAll();

    @Query("SELECT * FROM paymentregions WHERE id=:id")
    PaymentRegions getById(Integer id);

    @Query("DELETE FROM paymentregions")
    void deleteAll();
}
