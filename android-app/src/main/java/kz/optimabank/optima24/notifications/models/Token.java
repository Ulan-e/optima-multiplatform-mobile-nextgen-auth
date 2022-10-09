package kz.optimabank.optima24.notifications.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Token {

    @SerializedName("clientId")
    @Expose
    private String clientId;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("tokenVersion")
    @Expose
    private String tokenVersion;
    @SerializedName("tokenSession")
    @Expose
    private String tokenSession;
    @SerializedName("appName")
    @Expose
    private String appName;
    @SerializedName("appVersion")
    @Expose
    private String appVersion;
    @SerializedName("osType")
    @Expose
    private String osType;
    @SerializedName("osVersion")
    @Expose
    private String osVersion;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("registrationDate")
    @Expose
    private String registrationDate;
    @SerializedName("lastSuccessSentDate")
    @Expose
    private String lastSuccessSentDate;
    @SerializedName("lastFailedSentDate")
    @Expose
    private String lastFailedSentDate;
    @SerializedName("_id")
    @Expose
    private String id;

    public String getClientId() {
        return clientId;
    }

    public String getToken() {
        return token;
    }

    public String getTokenVersion() {
        return tokenVersion;
    }

    public String getTokenSession() {
        return tokenSession;
    }

    public String getAppName() {
        return appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String getOsType() {
        return osType;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public String getStatus() {
        return status;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public String getLastSuccessSentDate() {
        return lastSuccessSentDate;
    }

    public String getLastFailedSentDate() {
        return lastFailedSentDate;
    }

    public String getId() {
        return id;
    }
}