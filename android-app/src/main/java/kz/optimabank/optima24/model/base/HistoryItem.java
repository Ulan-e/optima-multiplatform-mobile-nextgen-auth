package kz.optimabank.optima24.model.base;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import kz.optimabank.optima24.utility.Constants;

public class HistoryItem implements Serializable {
    @SerializedName("Id")
    public int id;
    public String name;
    @SerializedName("RegDate")
    public String createDate;
    @SerializedName("Amount")
    private double amount;
    @SerializedName("Fee")
    private double Fee;
    @SerializedName("Currency")
    private String currency;
    @SerializedName("AccountNumber")
    private String AccountNumber;
    @SerializedName("DocumentNumber")
    private String documentNumber;

    public HistoryItem() {}

    public HistoryItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public double getAmount() {
        return amount;
    }

    public String getAccountNumber() {
        return AccountNumber;
    }

    public double getFee() {
        return Fee;
    }

    public String getCurrency() {
        return currency;
    }


    public String getOperationDate(SimpleDateFormat simpleDateFormat) {
        String stDate = null;
        SimpleDateFormat sdf = getSimpleDateFormat();
        try {
            stDate = simpleDateFormat.format(sdf.parse(createDate));
            TimeUnit.MILLISECONDS.toDays(sdf.parse(createDate).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stDate;
    }

    public String getOperationTime() {
        String stDate = null;
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = getSimpleDateFormat();
        try {
            stDate = Constants.API_TIME_FORMAT.format(sdf.parse(createDate));
            TimeUnit.MILLISECONDS.toDays(sdf.parse(createDate).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stDate;
    }

    private SimpleDateFormat getSimpleDateFormat(){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf;
    }

    public static class PaymentHistoryItem extends HistoryItem {
        @SerializedName("ServiceName")
        private String serviceName;
        @SerializedName("BankResultCode")
        private int paymentStatus;
        @SerializedName("InvoiceId")
        private long InvoiceId;
        @SerializedName("ServiceId")
        public int ServiceId;
        @SerializedName("Reference")
        private String reference;
        @SerializedName("Status")
        public String status;
       @SerializedName("Success")
       public boolean success;
        @SerializedName("Parameters")
        private ArrayList<HistoryItemParameters> Parameters;

        public long getInvoiceId() {
            return InvoiceId;
        }


        public ArrayList<HistoryItemParameters> getParameters() {
            return Parameters;
        }



        public String getServiceName() {
            return serviceName;
        }

        public int getPaymentStatus() {
            return paymentStatus;
        }

        public String getReference() {
            return reference;
        }

        public String getPaymentStatus(Context context) {
            String status = "";
            switch (paymentStatus) {

            }
            return status;
        }
    }

    public static class TransferHistoryItem extends HistoryItem {
        @SerializedName("State")
        private int State;
        @SerializedName("Description")
        private String description;
        @SerializedName("Type")
        private int Type;
        @SerializedName("FeeCurrency")
        private String feeCurrency;
        @SerializedName("ContragentAccountNumber")
        private String contragentAccountNumber;
        @SerializedName("ContragentName")
        private String contragentName;
        @SerializedName("ContragentBicName")
        private String contragentBicName;
        @SerializedName("ReceiverRequisites")
        private String receiverRequisites;
        @SerializedName("TransferMethod")
        private int transferMethod;

        public String getReceiverRequisites() {
            return receiverRequisites;
        }

        public void setReceiverRequisites(String receiverRequisites) {
            this.receiverRequisites = receiverRequisites;
        }

        public int getTransferMethod() {
            return transferMethod;
        }

        public void setTransferMethod(int transferMethod) {
            this.transferMethod = transferMethod;
        }

        public int getType() {
            return Type;
        }

        public int getState() {
            return State;
        }

        public String getContragentAccountNumber() {
            return contragentAccountNumber;
        }

        public String getContragentName() {
            return contragentName;
        }

        public String getContragentBicName() {
            return contragentBicName;
        }

        public String getDescription() {
            return description;
        }

        public String getFeeCurrency() {
            return feeCurrency;
        }
    }

    public class HistoryItemParameters implements Serializable {
        @SerializedName("Label")
        private String Label;
        @SerializedName("Value")
        private String Value;
        @SerializedName("Id")
        private int Id;
        @SerializedName("PaymentInfoId")
        private int PaymentInfoId;
        @SerializedName("ServiceParameterId")
        private int ServiceParameterId;

        public int getId() {
            return Id;
        }

        public int getPaymentInfoId() {
            return PaymentInfoId;
        }

        public int getServiceParameterId() {
            return ServiceParameterId;
        }

        public String getLabel() {
            return Label;
        }

        public String getValue() {
            return Value;
        }
    }
}
