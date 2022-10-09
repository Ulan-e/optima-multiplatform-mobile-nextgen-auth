package kz.optimabank.optima24.model.gson.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TaxDictResponse {
    @SerializedName("Rayons")
    public ArrayList<Identity> rayons;

    @SerializedName("Settlements")
    public ArrayList<Identity> settlements;

    public static class Identity {
        @SerializedName("Id")
        public int id;
        @SerializedName("Code")
        public String code;
        @SerializedName("Name")
        public String name;
        @SerializedName("RegionCode")
        public int regionCode;
        @SerializedName("RayonCode")
        public int rayonCode;
    }
}
