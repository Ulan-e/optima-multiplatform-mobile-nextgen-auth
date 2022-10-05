package kz.optimabank.optima24.db.entry;

import android.annotation.TargetApi;
import android.os.Build;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import kz.optimabank.optima24.model.base.PaymentServiceParameter;

/**
  Created by Timyr on 21.04.2017.
 */

@RealmClass
public class PaymentService extends RealmObject implements Serializable {
    @PrimaryKey
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
    public RealmList<PaymentServiceParameter> parameters;
    @SerializedName("Fee")
    public double Fee;
    @SerializedName("IsInvoiceable")
    public boolean IsInvoiceable;
    @SerializedName("IsPenalties")
    public boolean IsPenalties;
    @SerializedName("Regions")
    public RealmList<Region> regions;

    public int getId() {
        return id;
    }

    public RealmList<PaymentServiceParameter> getParameters() {
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
