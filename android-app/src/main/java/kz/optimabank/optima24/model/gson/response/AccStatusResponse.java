package kz.optimabank.optima24.model.gson.response;

import com.google.gson.annotations.SerializedName;

public class AccStatusResponse {
    @SerializedName("AccountType")
    public int accountType;
    @SerializedName("Currency")
    public String currency;
    @SerializedName("OwnerName")
    public String ownerName;
    @SerializedName("IsCredit")
    public boolean isCredit;
    @SerializedName("IsDebit")
    public boolean isDebit;
    @SerializedName("BrandType")
    public int brandType;
}
