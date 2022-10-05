package kz.optimabank.optima24.room_db.daos;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import kz.optimabank.optima24.db.entry.PaymentCategory;

public interface PaymentCategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertAll(List<PaymentCategory> paymentCategories);

    @Query("SELECT * FROM paymentcategory")
    List<PaymentCategory> getAll();

    @Query("SELECT * FROM paymentcategory WHERE id=:id")
    List<PaymentCategory> getAllBYId(Integer id);

    @Query("SELECT * FROM paymentcategory WHERE id=:id")
    PaymentCategory getById(Integer id);

    @Query("DELETE FROM paymentcategory")
    void deleteAll();
}
