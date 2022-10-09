package kz.optimabank.optima24.model.base;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import kz.optimabank.optima24.utility.Constants;

public  class Templates implements Serializable {
    public Templates() {
    }

    public Templates(int code, String name) {
        this.name = name;
        this.code = code;
    }

    public int code = Constants.ITEM_ID;
    public String name;

    @SerializedName("CreateDate")
    public String createDate;
}
