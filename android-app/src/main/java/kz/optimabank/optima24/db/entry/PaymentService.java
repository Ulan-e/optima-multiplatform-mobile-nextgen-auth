package kz.optimabank.optima24.db.entry;

import android.annotation.TargetApi;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import kz.optimabank.optima24.model.base.PaymentServiceParameter;

/**
  Created by Timyr on 21.04.2017.
 */

@Entity
public class PaymentService implements Serializable {
    @PrimaryKey
    @NonNull
    @SerializedName("Alias")
    public String alias;
    @SerializedName("Id")
    public int id;
    @SerializedName("ExternalId")
    public String ExternalId;
    @SerializedName("AllowedForGetBalance")
    public boolean isAllowedGetBalance;
    @SerializedName("IsFixedAmount")
    public boolean isFixedAmount ;
    @SerializedName("Name")
    public String name;
    @SerializedName("Description")
    public String description;
    @SerializedName("PaymentCategoryId")
    public int paymentCategoryId;
    @SerializedName("Parameters")

    @Ignore
    public List<PaymentServiceParameter> parameters;
    @SerializedName("Fee")
    public double Fee;
    @SerializedName("IsInvoiceable")
    public boolean IsInvoiceable;
    @SerializedName("IsPenalties")
    public boolean IsPenalties;

    @SerializedName("Regions")
    @Ignore
    public List<Region> regions;

    public int getId() {
        return id;
    }

    public List<PaymentServiceParameter> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return "PaymentService{" +
                "alias='" + alias + '\'' +
                ", id=" + id +
                ", ExternalId=" + ExternalId +
                ", isAllowedGetBalance=" + isAllowedGetBalance +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", paymentCategoryId=" + paymentCategoryId +
                ", parameters=" + parameters +
                ", Fee=" + Fee +
                ", IsInvoiceable=" + IsInvoiceable +
                ", IsPenalties=" + IsPenalties +
                ", regions=" + regions +
                '}';
    }

    public boolean containsRegion(int regionId){
        for (Region region : regions){
            if (regionId == region.getId())
                return true;
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public int[] getRegions() {
        return regions.stream().mapToInt(region -> region.getId()).toArray();
    }
}
