package kz.optimabank.optima24.model.gson.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Bool implements Serializable {
    @SerializedName("IsActive")
    private boolean IsActive;


    public boolean getIsActive() {
        return IsActive;
    }
}
