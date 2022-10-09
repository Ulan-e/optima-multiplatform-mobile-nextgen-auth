package kz.optimabank.optima24.model.gson.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import kz.optimabank.optima24.model.base.PenaltyItem;

/**
  Created by Timur on 17.05.2017.
 */

public class CheckPaymentsResponse implements Serializable {
    @SerializedName("Penalties")
    public ArrayList<PenaltyItem> penalties;
    @SerializedName("Balance")
    public double balance;
    @SerializedName("Amount")
    public double amount;
    @SerializedName("FixComm")
    public double fixComm;
    @SerializedName("PrcntComm")
    public double prcntComm;
    @SerializedName("IsTemplateExist")
    public boolean isTemplateExist;
    @SerializedName("MinComm")
    public double minComm;
    @SerializedName("Fee")
    public double fee;
    @SerializedName("ClientInfo")
    public String clientInfo;
    @SerializedName("ClientAddress")
    public String clientAddress;
    @SerializedName("Invoices")
    public ArrayList<Invoices> invoices;
    @SerializedName("ProvReference")
    public String provReference;
    @SerializedName("IsNeedSmsConfirmation")
    public boolean isNeedSmsConfirmation;
}
