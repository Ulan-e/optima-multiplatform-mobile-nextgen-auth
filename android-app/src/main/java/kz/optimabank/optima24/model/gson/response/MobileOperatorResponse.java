package kz.optimabank.optima24.model.gson.response;

import com.google.gson.annotations.SerializedName;

/**
 * @ Created by fitsa on 17.06.2016.
 */
public class MobileOperatorResponse {
    @SerializedName("PhoneNumber")
    public String phoneNumber;
    @SerializedName("Ported")
    public boolean ported;
    @SerializedName("Operator")
    public String operator;
    @SerializedName("ServiceId")
    public int serviceId;
}
