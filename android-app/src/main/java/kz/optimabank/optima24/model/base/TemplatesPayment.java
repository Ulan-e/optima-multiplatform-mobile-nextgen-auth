package kz.optimabank.optima24.model.base;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Date;

/**
  Created by Timur on 25.04.2017.
 */

public class TemplatesPayment extends Templates implements Serializable {
    @SerializedName("Id")
    public int id;
    @SerializedName("Name")
    public String name;
    @SerializedName("ServiceId")
    public int serviceId;
    @SerializedName("SourceAccountId")
    public int sourceAccountId;
    @SerializedName("Amount")
    public double amount;
    @SerializedName("Parameters")
    public ArrayList<TemplateParameter> parameters;
    @SerializedName("AutoPayTime")
    public String AutoPayTime;
    @SerializedName("PayAmountType")
    private int PayAmountType;
    @SerializedName("AutoPayDate")
    public String AutoPayDate;
    @SerializedName("AutoPayType")
    public int AutoPayType;
    @SerializedName("AutoTransferCount")
    private int AutoPayCount;
    @SerializedName("AutoPayStatus")
    public int autoPayStatus2;
    @SerializedName("StandingInstructionStatus")
    public int autoPayStatus;
    @SerializedName("IsAutoPay")
    public boolean isAutoPay;
    @SerializedName("IsActive")
    public boolean IsActive;
    @SerializedName("AutoTransferDateBegin")
    private Date AutoPayDateBegin;
    @SerializedName("StartPayDay")
    public String StartPayDay;

    public String getAmount() {
        BigDecimal bd = new BigDecimal(amount);
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');
        DecimalFormat formatter = new DecimalFormat("###,##0.00", symbols);
//        return String.format("%.2f %s", balance, Currency);
        return formatter.format(bd.doubleValue()) + " ";
    }

    public class TemplateParameter implements Serializable{
        @SerializedName("Id")
        private int id;
        @SerializedName("ServiceParameterId")
        private int serviceParameterId;
        @SerializedName("SubscriptionId")
        private int subscriptionId;
        @SerializedName("Value")
        private String value;

        public int getId() {
            return id;
        }

        public int getServiceParameterId() {
            return serviceParameterId;
        }

        public int getSubscriptionId() {
            return subscriptionId;
        }

        public String getValue() {
            if (value == null) {
                value = "";
            }
            return value.replaceAll("\\s", "");
        }

        @Override
        public String toString() {
            return "TemplateParameter{" +
                    "id=" + id +
                    ", serviceParameterId=" + serviceParameterId +
                    ", subscriptionId=" + subscriptionId +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "TemplatesPayment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", serviceId=" + serviceId +
                ", sourceAccountId=" + sourceAccountId +
                ", amount=" + amount +
                ", parameters=" + parameters +
                ", AutoPayTime='" + AutoPayTime + '\'' +
                ", PayAmountType=" + PayAmountType +
                ", AutoPayDate='" + AutoPayDate + '\'' +
                ", AutoPayType=" + AutoPayType +
                ", AutoPayCount=" + AutoPayCount +
                ", autoPayStatus2=" + autoPayStatus2 +
                ", autoPayStatus=" + autoPayStatus +
                ", isAutoPay=" + isAutoPay +
                ", IsActive=" + IsActive +
                ", AutoPayDateBegin=" + AutoPayDateBegin +
                '}';
    }

    public int getId() {
        return id;
    }
}
