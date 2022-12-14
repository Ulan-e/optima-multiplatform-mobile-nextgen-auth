package kz.optimabank.optima24.local.daos;

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

    @Query("SELECT * FROM paymentservice WHERE paymentCategoryId=:categoryId")
    List<PaymentService> getByCategoryId(Integer categoryId);

    @Query("SELECT * FROM paymentservice WHERE id=:id")
    PaymentService getById(Integer id);

    @Query("SELECT * FROM paymentservice WHERE ExternalId=:externalId")
    PaymentService getByExternalId(Integer externalId);

    @Query("DELETE FROM paymentservice")
    void deleteAll();
}
