package kz.optimabank.optima24.model.base;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class CardLimit implements  Serializable{

    @SerializedName("IsActive")
    public boolean IsActive;
    @SerializedName("TypeLimit")
    public String TypeLimit;
    @SerializedName("Region")
    public String Region;
    @SerializedName("Amount")
    public String Amount;
    @SerializedName("SingleAmount")
    public String SingleAmount;
    @SerializedName("MaxNumber")
    public String MaxNumber;
    @SerializedName("Period")
    public String Period;
    @SerializedName("Sms")
    public String Sms;
    @SerializedName("Id")
    public int Id;

}