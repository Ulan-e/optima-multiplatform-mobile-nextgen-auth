package kz.optimabank.optima24.room_db.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import kz.optimabank.optima24.notifications.models.Notification;

@Dao
public interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insert(Notification notification);

    @Query("SELECT * FROM notification WHERE bankId=:bankId")
    Single<List<Notification>> getAll(String bankId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<List<Long>> insertAll(List<Notification> notifications);

    @Query("SELECT * FROM notification WHERE id=:id")
    Single<Notification> findById(String id);

    @Update
    Completable update(Notification notification);

    @Delete
    Completable delete(Notification notification);

    @Query("DELETE FROM notification")
    Completable deleteAll();
}