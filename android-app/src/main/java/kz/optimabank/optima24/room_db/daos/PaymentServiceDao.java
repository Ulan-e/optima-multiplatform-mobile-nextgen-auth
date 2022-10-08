package kz.optimabank.optima24.room_db.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import kz.optimabank.optima24.db.entry.PaymentService;

@Dao
public interface PaymentServiceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<PaymentService> paymentServices);

    @Query("SELECT * FROM paymentservice")
    List<PaymentService> getAll();

    @Query("SELECT * FROM paymentservice WHERE id=:id")
    List<PaymentService> getAll(Integer id);

    @Query("SELECT * FROM paymentservice WHERE id=:id")
    PaymentService getById(Integer id);

    @Query("DELETE FROM paymentservice")
    void deleteAll();
}
