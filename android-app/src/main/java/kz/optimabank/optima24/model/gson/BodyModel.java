package kz.optimabank.optima24.model.gson;


import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;

import java.io.Serializable;

import kz.optimabank.optima24.model.base.Limit;

/**
 * Created by Timur on 12.05.2017.
 */

public class BodyModel implements Serializable {

    public static class Mt100Transfer extends BodyModel {
        public int accountCode;
        public int productType;
        public int type;
        public int Id;
        public int transferType;
        public String contragentAccountCode;
        public String contragentAccountNumber;
        public String accountNumber;
        public String currency;
        public String contragentName;
        public String contragentIdn;
        public String contragentSeco;
        public String contragentBic;
        public String operationKnp;
        public String purpose;
        public String feeAmount;
        public String feeCurrency;
        public String contragentCurrency;
        public String ClientShortName;
        public String RateInfo;
        public String ContragentRbsNumber;
        public String ContragentAccountId;
        public String ContragentBicName;
        public String OperationKbk;
        public String InnerOperation;
        public String amount;
        public String CreateDate;
        public String securityCode;
        public int ContragentCardBrandType;
        public boolean contragentResident;
        @SerializedName("OperationCode")
        public String operationCode;
        @SerializedName("RequestId")
        public long requestId;
        @SerializedName("TransferMethod")
        public String transferMethod;
        @SerializedName("ReceiverRequisites")
        public String receiverRequisites;
    }

    public static class SwiftTransfer extends BodyModel {
        @SerializedName("AccountCode")
        public int accountCode;
        @SerializedName("ProductType")
        public int productType;
        @Nullable
        public Integer FeeAccountCode;
        public String contragentName;
        public String contragentIdn;
        public String ContragentKpp;
        public String ContragentCountry;
        public String ContragentRegistrationCountry;
        public String ContragentAddress;
        public String ContragentAccountNumber;
        public String contragentBic;
        public String ContragentBicName;
        public String ContragentBankAccountNumber;
        @SerializedName("OperationKnp")
        public String operationKnp;
        @SerializedName("Purpose")
        public String purpose;
        @SerializedName("Amount")
        public double amount;
        @SerializedName("Type")
        public int type = 2;
        @SerializedName("Currency")
        public String currency;
        public String InnerOperation;
        public String ChargesType;
        public String PayerName;
        public String PayerCountry;
        public String PayerAddress;
        public String IntermediaryName;
        public String IntermediaryBic;
        @SerializedName("SecurityCode")
        public String securityCode;
        @SerializedName("FeeAmount")
        public double feeAmount;
        @SerializedName("FeeCurrency")
        public String feeCurrency;
        @SerializedName("OperationCode")
        public String operationCode;
        @SerializedName("RequestId")
        public long requestId;
    }

    public static class PaymentCheck extends BodyModel {
        @SerializedName("PaymentServiceId")
        public int PaymentServiceId;
        @SerializedName("Amount")
        public String Amount;
        public boolean isAutoPay;
        @SerializedName("AccountId")
        public String AccountId;
        @SerializedName("FixComm")
        public String FixComm;
        @SerializedName("PrcntComm")
        public String PrcntComm;
        @SerializedName("MinComm")
        public String MinComm;
        @SerializedName("ProvReference")
        public String ProvReference;
        @SerializedName("ConfirmCode")
        public String ConfirmCode = "";
        @SerializedName("Parameters")
        public JSONArray Parameters = new JSONArray();
        @SerializedName("OperationCode")
        public String operationCode;
        @SerializedName("RequestId")
        public long requestId;
    }

    public static class CreateTemplate extends BodyModel {
        @SerializedName("ServiceId")
        public String ServiceId;
        @SerializedName("Amount")
        public String Amount;
        @SerializedName("IsAutoPay")
        public boolean IsAutoPay;
        public boolean IsActive = true;
        @SerializedName("Name")
        public String Name;
        public String documentId;
        @SerializedName("SourceAccountId")
        public String SourceAccountId;
        @SerializedName("AutoPayType")
        public String AutoPayType;
        @SerializedName("AutoPayDate")
        public String AutoPayDate;
        @SerializedName("AutoPayTime")
        public String AutoPayTime;
        @SerializedName("StartPayDay")
        public String StartPayDay;
        @SerializedName("ConfirmCode")
        public String ConfirmCode;
        @SerializedName("Parameters")
        public JSONArray Parameters;
        @SerializedName("OperationCode")
        public String operationCode;
        @SerializedName("RequestId")
        public long requestId;
    }

    public static class SetLimit extends BodyModel {
        public String Sms;
        public Limit Limits;
    }

    public static class PaymentInvoice extends BodyModel {
        public int paymentServiceId;
        public long invoiceId;
        public String accountId;
        public String fixComm;
        public String prcntComm;
        public String minComm;
        public JSONArray parameters = new JSONArray();
    }
}
