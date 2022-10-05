package kz.optimabank.optima24.notifications.data.local;

import android.content.Context;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import kz.optimabank.optima24.notifications.models.Notification;
import kz.optimabank.optima24.room_db.AppDatabase;
import kz.optimabank.optima24.room_db.daos.NotificationDao;

public class NotificationsLocalRepositoryImpl implements NotificationsLocalRepository {

    private static final String TAG = "NotificationsLocalRepository";

    private NotificationDao notificationDao;

    public NotificationsLocalRepositoryImpl(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        this.notificationDao = database.notificationDao();
    }

    @Override
    public Single<Long> insert(Notification notification) {
        Log.d(TAG, "insert " + notification);
        return notificationDao.insert(notification);
    }

    @Override
    public Single<List<Long>> putNotifications(List<Notification> notifications, String bankId) {
        Log.d(TAG, "putNotifications size " + notifications.size());
        List<Notification> all = new ArrayList<>();
        for (Notification item : notifications) {
            all.add(Notification.setNotificationBankId(item, bankId));
        }
        return notificationDao.insertAll(all);
    }

    @Override
    public Completable deleteAllNotifications() {
        Log.d(TAG, "deleteAllNotifications ");
        return notificationDao.deleteAll();
    }

    @Override
    public Completable update(Notification notification) {
        Log.d(TAG, "update " + notification);
        return notificationDao.update(notification);
    }

    @Override
    public Single<List<Notification>> getNotifications(String bankId) {
        return notificationDao.getAll(bankId);
    }

    @Override
    public Single<Notification> findNotificationById(@NotNull String id) {
        return notificationDao.findById(id);
    }

    @Override
    public void closeDatabase() {
        AppDatabase.destroyInstance();
    }
}