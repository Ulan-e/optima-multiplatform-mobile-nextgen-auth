package kz.optimabank.optima24.notifications.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Notification {

    @SerializedName("id")
    @Expose
    @PrimaryKey
    @NonNull
    private String id;
    @SerializedName("sendedDate")
    @Expose
    private String sentDate;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("text")
    @Expose
    private String text;

    private boolean isRead = false;

    private String bankId;

    public String getBankId() {
        return bankId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        this.isRead = read;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSentDate() {
        return sentDate;
    }

    public void setSentDate(String date) {
        this.sentDate = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @NonNull
    @Override
    public String toString() {
        return "Notification (id " + id +
                " sentDate " + sentDate +
                " title " + title +
                " text " + text +
                " isRead " + isRead +
                ")";
    }

    // получаем новое уведомление и ставим галочку прочитанным
    public static Notification makeRead(Notification notification, String bankId) {
        Notification newNotification = new Notification();
        newNotification.setId(notification.getId());
        newNotification.setSentDate(notification.getSentDate());
        newNotification.setTitle(notification.getTitle());
        newNotification.setText(notification.getText());
        newNotification.setBankId(bankId);
        newNotification.setRead(true);
        return newNotification;
    }

    // получаем новое уведомление и ставим галочку прочитанным
    public static Notification setNotificationBankId(Notification notification, String bankId) {
        Notification newNotification = new Notification();
        newNotification.setId(notification.getId());
        newNotification.setSentDate(notification.getSentDate());
        newNotification.setTitle(notification.getTitle());
        newNotification.setText(notification.getText());
        newNotification.setBankId(bankId);
        newNotification.setRead(false);
        return newNotification;
    }
}