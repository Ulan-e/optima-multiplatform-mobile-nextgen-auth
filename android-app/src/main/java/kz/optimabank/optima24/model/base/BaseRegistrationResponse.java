package kz.optimabank.optima24.model.base;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BaseRegistrationResponse implements Serializable {
    public String message;
    public ResultCodes code;
    public int data;

    public enum ResultCodes {
        @SerializedName("-11000017") ACTIVE_USER,
        @SerializedName("-11000019") BLOCKED_USER,
        @SerializedName("-11000015") REG_FOR_CLIENT,
        @SerializedName("0") SUCCESS,
        @SerializedName(value = "code", alternate = {"-11000016", "-11000013"}) REG_FOR_NO_CLIENT,
        @SerializedName("-11000001") SAME_IDN,
        @SerializedName("-11000002") SAME_PHONE,
        @SerializedName("-11000014") INCORRECT_PHONE,
        @SerializedName("-10001") INCORRECT_SMS,
        @SerializedName("-10004") DOESNT_EXPIRED_SMS,
        @SerializedName("-11000007") NO_CLIENT_WITH_HASH,
        @SerializedName("-100") INCORRECT_OLD_PASSWORD,
        @SerializedName("-11000018") UNEXPECTED_STATUS_USER
    }
}
