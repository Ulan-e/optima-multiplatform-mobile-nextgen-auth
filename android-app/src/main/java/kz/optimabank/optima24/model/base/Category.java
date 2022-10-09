package kz.optimabank.optima24.model.base;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Category extends Templates implements Serializable {
    @SerializedName("Name")
    public String Name;
    @SerializedName("ParentId")
    public int ParentId;
    @SerializedName("IsDefault")
    public boolean IsDefault;
    @SerializedName("Id")
    public int ID;

    public String getName() {
        return Name;
    }

}