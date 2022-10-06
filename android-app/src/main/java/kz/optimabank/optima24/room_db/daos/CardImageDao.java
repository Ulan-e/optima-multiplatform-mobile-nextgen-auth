package kz.optimabank.optima24.room_db.daos;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import kz.optimabank.optima24.db.entry.Country;
import kz.optimabank.optima24.db.entry.DigitizedCard;

public interface CardImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(DigitizedCard digitizedCard);

    @Query("SELECT * FROM digitizedCard WHERE userPhone=:phone")
    List<DigitizedCard> getAll(String phone);

    @Query("SELECT * FROM digitizedcard WHERE rbsNumber=:rbsNumber and userPhone=:phone")
    DigitizedCard getByRbsNumber(String rbsNumber, String phone);

    @Query("DELETE FROM digitizedcard")
    void delete();
}
