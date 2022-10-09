package kz.optimabank.optima24.notifications.models.items;

public abstract class BaseNotificationItem {

    public static final int HEADER_ITEM = 0;
    public static final int NOTIFICATION_ITEM = 1;

    abstract public int getType();
}