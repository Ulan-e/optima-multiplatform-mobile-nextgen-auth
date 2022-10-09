package kz.optimabank.optima24.model.base;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CancelApplicationModel implements Serializable {
    @SerializedName("Id")
    public String id;

    @SerializedName("Comment")
    public String comment;

}