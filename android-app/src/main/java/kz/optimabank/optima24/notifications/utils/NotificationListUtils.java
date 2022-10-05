package kz.optimabank.optima24.notifications.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import kz.optimabank.optima24.notifications.models.Notification;
import kz.optimabank.optima24.notifications.models.items.BaseNotificationItem;
import kz.optimabank.optima24.notifications.models.items.HeaderItem;
import kz.optimabank.optima24.notifications.models.items.NotificationItem;

import static kz.optimabank.optima24.utility.DateConverterUtils.dateFromString;
import static kz.optimabank.optima24.utility.DateConverterUtils.isTheSameDay;

public class NotificationListUtils {

    // сортировка уведомлений по убыванию
    public static void sortByDateDesc(List<Notification> notifications) {
        if(notifications == null) return;
        Collections.sort(notifications, (thisDate, otherDate) -> {
            if (dateFromString(thisDate.getSentDate()) == null || dateFromString(otherDate.getSentDate()) == null) return 0;
            return dateFromString(thisDate.getSentDate()).compareTo(dateFromString(otherDate.getSentDate()));
        });
        Collections.reverse(notifications);
    }

    // разделяем уведомления по дням
    public static List<BaseNotificationItem> splitByDate(List<Notification> requestedNotification) {
        List<BaseNotificationItem> items = new ArrayList<>();
        Date date = null;
        for (Notification notification : requestedNotification) {
            if (!isTheSameDay(dateFromString(notification.getSentDate()), date)) {
                date = dateFromString(notification.getSentDate());
                items.add(new HeaderItem(date));
            }
            items.add(new NotificationItem(notification));
        }
        return items;
    }
}