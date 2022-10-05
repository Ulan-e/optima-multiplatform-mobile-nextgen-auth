package kz.optimabank.optima24.model.base;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import kz.optimabank.optima24.db.entry.PredefinedValues;

/**
  Created by Timur on 21.04.2017.
 */

public class PaymentServiceParameter implements Serializable {
    @SerializedName("Description")
    private String description;
    @SerializedName("ExternalId")
    private String externalId;
    @SerializedName("Id")
    public int id;
    @SerializedName("Name")
    public String name;
    @SerializedName("Mask")
    private String Mask;
    @SerializedName("Value")
    public String value;
    @SerializedName("UserSample")
    private String UserSample;
    @SerializedName("Length")
    private int length;
    @SerializedName("PredefinedValues")
    public List<PredefinedValues> predefinedValues;


    public int getId() {
        return id;
    }

    public String getUserSample() {
        return UserSample;
    }

    public String getMask() {
        return Mask;
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }

    public String getExternalId() {
        return externalId;
    }
}
