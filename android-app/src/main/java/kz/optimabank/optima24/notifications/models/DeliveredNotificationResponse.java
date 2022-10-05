package kz.optimabank.optima24.notifications.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliveredNotificationResponse {

    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public NotificationData data;

    public static class NotificationData {
        @SerializedName("source")
        @Expose
        public String source;
        @SerializedName("sourceType")
        @Expose
        public String sourceType;
        @SerializedName("sourceIp")
        @Expose
        public String sourceIp;
        @SerializedName("priority")
        @Expose
        public int priority;
        @SerializedName("apiService")
        @Expose
        public String apiService;
        @SerializedName("receivingDate")
        @Expose
        public String receivingDate;
        @SerializedName("receivingId")
        @Expose
        public Object receivingId;
        @SerializedName("title")
        @Expose
        public String title;
        @SerializedName("text")
        @Expose
        public String text;
        @SerializedName("requisite")
        @Expose
        public String requisite;
        @SerializedName("requisiteType")
        @Expose
        public String requisiteType;
        @SerializedName("sendingType")
        @Expose
        public String sendingType;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("statusTime")
        @Expose
        public String statusTime;
    }
}