package kz.optimabank.optima24.db.entry;

import com.google.gson.annotations.SerializedName;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

/**
  Created by Максим on 01.08.2017.
 */

@Entity
public class PaymentRegions {
    @SerializedName("Alias")
    private String alias;
    @SerializedName("ExternalId")
    private String externalId;
    @SerializedName("Id")
    @PrimaryKey
    @NonNull
    public int id;
    @SerializedName("Name")
    private String name;

    public PaymentRegions(int id, String name){
        this.id = id;
        this.name = name;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Ignore
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
