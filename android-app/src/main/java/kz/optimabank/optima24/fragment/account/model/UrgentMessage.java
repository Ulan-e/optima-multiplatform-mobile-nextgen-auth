package kz.optimabank.optima24.fragment.account.model;

import com.google.gson.annotations.SerializedName;

public class UrgentMessage {

    @SerializedName("Id")
    public int id;

    @SerializedName("Message")
    public String message;

    @SerializedName("BegDateTime")
    public String begDateTime;

    @SerializedName("EndDateTime")
    public String endDateTime;

    @SerializedName("Locale")
    public String locale;

    @SerializedName("IsActive")
    public boolean isActive;

    public void setId(int id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setBegDateTime(String begDateTime) {
        this.begDateTime = begDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getBegDateTime() {
        return begDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public String getLocale() {
        return locale;
    }

    public boolean getActive() {
        return isActive;
    }
}