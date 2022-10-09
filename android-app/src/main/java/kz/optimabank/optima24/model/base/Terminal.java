package kz.optimabank.optima24.model.base;

import android.location.Location;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import kz.optimabank.optima24.model.manager.GeneralManager;

/**
 * Created by Жексенов on 14.11.2014.
 * <p/>
 * Terminals and branches has are the same parameters, so that I won't override it to branches
 */
public class Terminal implements Comparable<Terminal>, Serializable, Cloneable{
    //        private static final long serialVersionUID = 7367936263743995287L;
    @SerializedName("PointType")
    private int PointType;
    @SerializedName("Id")
    private int Id;
    @SerializedName("Address")
    private String Address;
    @SerializedName("BranchCode")
    private String BranchCode;
    @SerializedName("City")
    private String City;
    @SerializedName("Latitude")
    private String Latitude;
    @SerializedName("Longitude")
    private String Longitude;
    @SerializedName("Note")
    private String Note;
    @SerializedName("Name")
    private String Name;
    @SerializedName("Number")
    private String Number;
    @SerializedName("WorkTime")
    private String WorkTime;
    private int distance;
    @SerializedName("Status")
    private int Status;


    private boolean Is24HAvailable, IsCashIn, IsInBank;


    public int getStatus() {
        return Status;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDistanceD() {
        return distance;
    }

    public int getId() {
        return Id;
    }

    public String getAddress() {
        return Address;
    }

    public String getBranchCode() {
        return BranchCode;
    }

    public String getCity() {
        return City;
    }

    //    Latitude
    public String getLatitude() {
        return Latitude;
    }

    //    Latitude
    public String getLongitude() {
        return Longitude;
    }

    public String getNote() {
        return Note;
    }

    public String getName() {
        return Name;
    }

    public String getNumber() {
        return Number;
    }

    public String getWorkTime() {
        return WorkTime;
    }

    public int getDistance() {
        Location location = GeneralManager.getInstance().getUserLocation();
        if(location!=null) {
            Location locationA = new Location("point A");
            locationA.setLatitude(GeneralManager.getInstance().getUserLocation().getLatitude());
            locationA.setLongitude(GeneralManager.getInstance().getUserLocation().getLongitude());

            Location locationB = new Location("point B");
            locationB.setLatitude(Double.parseDouble(getLatitude()));
            locationB.setLongitude(Double.parseDouble(getLongitude()));

            return (int) locationA.distanceTo(locationB);
        } else {
            return 0;
        }
    }

    public int getPointType() {
        return PointType;
    }

    public boolean getIs24HAvailable() {
        return Is24HAvailable;
    }

    public boolean getIsCashIn() {
        return IsCashIn;
    }

    public boolean getIsInBank() {
        return IsInBank;
    }


    @Override
    public int compareTo(Terminal another) {
        return ((Integer) getDistance()).compareTo(another.getDistance());
    }

    public Terminal clone() throws CloneNotSupportedException {
        return (Terminal)super.clone();
    }
}