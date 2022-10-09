package kz.optimabank.optima24.notifications.models.items;

import androidx.annotation.NonNull;

import java.util.Date;

public class HeaderItem extends BaseNotificationItem {

    private Date date;

    public HeaderItem(@NonNull Date date) {
        this.date = date;
    }

    @NonNull
    public Date getDate() {
        return date;
    }

    @Override
    public int getType() {
        return HEADER_ITEM;
    }
}