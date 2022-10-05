package kz.optimabank.optima24.model.gson.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BankReference implements Serializable {
    @SerializedName("BankReference")
    private String BankReference;
    @SerializedName("Reference")
    private String reference;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getBankReference() {
        return BankReference;
    }

    public void setBankReference(String bankReference) {
        BankReference = bankReference;
    }
}
