package kz.optimabank.optima24.notifications.data.local;

import androidx.annotation.NonNull;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import kz.optimabank.optima24.notifications.models.Notification;

public interface NotificationsLocalRepository {

    Single<Long> insert(Notification notification);

    Single<List<Long>> putNotifications(List<Notification> notifications, String bankId);

    Completable update(Notification notification);

    Completable deleteAllNotifications();

    Single<List<Notification>> getNotifications(String bankId);

    Single<Notification> findNotificationById(@NonNull String id);

    void closeDatabase();
}