package kz.optimabank.optima24.notifications.models.items;

import androidx.annotation.NonNull;

import kz.optimabank.optima24.notifications.models.Notification;

public class NotificationItem extends BaseNotificationItem {

    private Notification notification;

    public NotificationItem(@NonNull Notification notification) {
        this.notification = notification;
    }

    @NonNull
    public Notification getNotification() {
        return notification;
    }

    @Override
    public int getType() {
        return NOTIFICATION_ITEM;
    }
}