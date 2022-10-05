package kz.optimabank.optima24.model.gson.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
  Created by Timur on 12.05.2017.
 */

public class CheckResponse implements Serializable{
    @SerializedName("FeeAmount")
    public String feeAmount;
    @SerializedName("FeeCurrency")
    public String feeCurrency;
    @SerializedName("ConfirmText")
    public String confirmText;
    @SerializedName("IsNeedSmsConfirmation")
    public boolean isNeedSmsConfirmation;

    public String getFeeAmount() {
        return feeAmount;
    }

    public String getFeeCurrency() {
        return feeCurrency;
    }

    public String getConfirmText() {
        return confirmText;
    }

    public boolean isNeedSmsConfirmation() {
        return isNeedSmsConfirmation;
    }
}
