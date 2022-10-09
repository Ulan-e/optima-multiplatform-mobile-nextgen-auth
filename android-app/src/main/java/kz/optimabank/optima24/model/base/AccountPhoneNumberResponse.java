package kz.optimabank.optima24.model.base;

import com.google.gson.annotations.SerializedName;

public class AccountPhoneNumberResponse {
    @SerializedName("AccountType")
    public int accountType;
    @SerializedName("BrandType")
    public String brandType;
    @SerializedName("Currency")
    public String currency;
    @SerializedName("OwnerName")
    public String ownerName;
    @SerializedName("Number")
    public String CardNumber;
    @SerializedName("IsCredit")
    public boolean isCredit;
    @SerializedName("IsDebit")
    public boolean isDebit;

    public int getAccountType() {
        return accountType;
    }

    public String getBrandType() {
        return brandType;
    }

    public String getCurrency() {
        return currency;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public boolean isCredit() {
        return isCredit;
    }

    public boolean isDebit() {
        return isDebit;
    }

    public String getCardNumber() {
        return CardNumber;
    }
}