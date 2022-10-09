package kz.optimabank.optima24.model.base;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Limit implements Serializable, Parcelable {

    @SerializedName("Name")
    public String Name;
    @SerializedName("Amount")
    public String Amount;
    @SerializedName("SingleAmount")
    public String SingleAmount;
    @SerializedName("Number")
    public String Number;
    @SerializedName("Period")
    public String Period;
    @SerializedName("Status")
    public String Status;
    @SerializedName("Type")
    public int Type;
    @SerializedName("StartDate")
    public String StartDate;
    @SerializedName("EndDate")
    public String EndDate;
    @SerializedName("SmsCode")
    public String SmsCode;

    public Limit() {
    }

    public Limit(String name, String amount, String singleAmount, String number, String period, String status, int type, String startDate, String endDate, String smsCode) {
        Name = name;
        Amount = amount;
        SingleAmount = singleAmount;
        Number = number;
        Period = period;
        Status = status;
        Type = type;
        StartDate = startDate;
        EndDate = endDate;
        SmsCode = smsCode;
    }

    protected Limit(Parcel in) {
        Name = in.readString();
        Amount = in.readString();
        SingleAmount = in.readString();
        Number = in.readString();
        Period = in.readString();
        Status = in.readString();
        Type = in.readInt();
        StartDate = in.readString();
        EndDate = in.readString();
        SmsCode = in.readString();
    }

    public static final Creator<Limit> CREATOR = new Creator<Limit>() {
        @Override
        public Limit createFromParcel(Parcel in) {
            return new Limit(in);
        }

        @Override
        public Limit[] newArray(int size) {
            return new Limit[size];
        }
    };

    public boolean getStatus() {
        return Boolean.valueOf(Status.toLowerCase());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Name);
        dest.writeString(Amount);
        dest.writeString(SingleAmount);
        dest.writeString(Number);
        dest.writeString(Period);
        dest.writeString(Status);
        dest.writeInt(Type);
        dest.writeString(StartDate);
        dest.writeString(EndDate);
        dest.writeString(SmsCode);
    }

    public static class Internet {
        @SerializedName("IsActive")
        public boolean IsActive;
        @SerializedName("Code")
        public int Code;
        @SerializedName("DateFrom")
        public String DateFrom;
        @SerializedName("DateTo")
        public String DateTo;
        @SerializedName("SmsCode")
        public String SmsCode;
    }

    public static class JustClass {

    }
}