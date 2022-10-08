package kz.optimabank.optima24.db.entry;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
  Created by Timur on 13.05.2017.
 */

@Entity
public class Country implements Serializable {
    @SerializedName("AlphaCode")
    public String AlphaCode;
    @SerializedName("Name")
    @PrimaryKey
    @NonNull
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
