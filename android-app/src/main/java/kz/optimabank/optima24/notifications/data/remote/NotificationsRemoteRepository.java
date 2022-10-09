package kz.optimabank.optima24.notifications.data.remote;

import java.util.List;

import io.reactivex.Single;
import kz.optimabank.optima24.notifications.models.DeliveredNotificationResponse;
import kz.optimabank.optima24.notifications.models.Notification;
import kz.optimabank.optima24.notifications.models.NotificationIdBody;
import kz.optimabank.optima24.notifications.models.NotificationResponse;
import retrofit2.Response;

public interface NotificationsRemoteRepository {

    // получаем список всех уведомлений
    Single<List<Notification>> getUnreadNotifications(String bankId);

    // отправляем отчет о доставке
    Single<Response<DeliveredNotificationResponse>> requestNotificationDelivered(NotificationIdBody body);

    // делаем прочитанным уведомление в сервисе пушей
    Single<Response<NotificationResponse>> setReadNotification(NotificationIdBody body);
}