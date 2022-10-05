package kz.optimabank.optima24.db.entry;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

public class PredefinedValues extends RealmObject implements Serializable{
    @SerializedName("ExternalId")
    public String externalId;
    @SerializedName("Id")
    public int id;
    //@SerializedName("PaymentServiceParameterId")
    //public int paymentServiceParameterId;
    @SerializedName("Value")
    public String value;
}
