package kz.optimabank.optima24.model.base;

import android.annotation.SuppressLint;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import kz.optimabank.optima24.utility.Constants;

public class HistoryApplications implements Serializable {
    @SerializedName("DisplayName")
    public String DisplayName;

    @SerializedName("StatusDisplayName")
    public String StatusDisplayName;

    @SerializedName("Date")
    public String Date;

    @SerializedName("IsSaveApp")
    public boolean IsSaveApp;

    @SerializedName("Id")
    public int id;

    @SerializedName("StatusStyleType")
    public String StatusStyleType;

    public enum StyleTypeDto{
        SUCCESS(0),
        ERROR(1),
        DEFAULTBLUE(2),
        DEFAULTORANGE(3),
        ACTIVE(4);

        public final int type;

        StyleTypeDto(int value){this.type = value;}
    }

    public HistoryApplications(int id, String name) {
        this.id = id;
        this.DisplayName = name;
    }

    public String getOperationDate(SimpleDateFormat simpleDateFormat) {
        String stDate = null;
        SimpleDateFormat sdf = getSimpleDateFormat();
        try {
            stDate = simpleDateFormat.format(sdf.parse(Date));
            TimeUnit.MILLISECONDS.toDays(sdf.parse(Date).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stDate;
    }

    public String getOperationTime() {
        String stDate = null;
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = getSimpleDateFormat();
        try {
            stDate = Constants.API_TIME_FORMAT.format(sdf.parse(Date));
            TimeUnit.MILLISECONDS.toDays(sdf.parse(Date).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stDate;
    }

    private SimpleDateFormat getSimpleDateFormat(){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf;
    }

}