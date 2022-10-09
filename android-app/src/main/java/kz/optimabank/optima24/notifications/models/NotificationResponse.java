package kz.optimabank.optima24.notifications.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationResponse {

    @SerializedName("success")
    @Expose
    boolean isSuccess;

    @SerializedName("message")
    @Expose
    String message;

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }
}