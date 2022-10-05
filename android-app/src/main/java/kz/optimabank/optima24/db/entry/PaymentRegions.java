package kz.optimabank.optima24.db.entry;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
  Created by Максим on 01.08.2017.
 */

@RealmClass
public class PaymentRegions extends RealmObject {
    @PrimaryKey
    @SerializedName("Alias")
    private String alias;
    @SerializedName("ExternalId")
    private String externalId;
    @SerializedName("Id")
    public int id;
    @SerializedName("Name")
    private String name;

    public PaymentRegions(int id, String name){
        this.id = id;
        this.name = name;
    }

    public PaymentRegions(){}

    public String getAlias() {
        return alias;
    }

    public String getExternalId() {
        return externalId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "PaymentRegions{" +
                "alias='" + alias + '\'' +
                ", externalId='" + externalId + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
