package kz.optimabank.optima24.db.entry;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
  Created by Timur on 13.05.2017.
 */

public class Country extends RealmObject implements Serializable {
    @PrimaryKey
    @SerializedName("AlphaCode")
    public String AlphaCode;
    @SerializedName("Name")
    public String Name;
    @SerializedName("NumericCode")
    public String NumericCode;

    public String getName() {
        return Name;
    }

    public String getAlphaCode() {
        return AlphaCode;
    }

    public String getNumericCode() {
        return NumericCode;
    }
}
