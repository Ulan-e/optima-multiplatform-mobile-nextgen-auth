package kz.optimabank.optima24.receipt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ReceiptModel implements Parcelable {
    private String operationTime;
    private String receiptNumber;
    private String sendCardNumber;
    private String recipientName;
    private String recipientCardNumber;
    private String fee;
    private String amount;
    private String operationStatus;

    private String serviceName;
    private String accountNumber;
    private int type;

    public static final Parcelable.Creator<ReceiptModel> CREATOR
            = new Parcelable.Creator<ReceiptModel>() {
        public ReceiptModel createFromParcel(Parcel in) {
            return new ReceiptModel(in);
        }

        public ReceiptModel[] newArray(int size) {
            return new ReceiptModel[size];
        }
    };

    public ReceiptModel() {
    }

    private ReceiptModel(Parcel in) {
        operationTime = in.readString();
        receiptNumber = in.readString();
        sendCardNumber = in.readString();
        recipientName = in.readString();
        recipientCardNumber = in.readString();
        fee = in.readString();
        amount = in.readString();
        operationStatus = in.readString();

        serviceName = in.readString();
        accountNumber = in.readString();
        type = in.readInt();
    }

    public String getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime = operationTime;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getSendCardNumber() {
        return sendCardNumber;
    }

    public void setSendCardNumber(String sendCardNumber) {
        this.sendCardNumber = sendCardNumber;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientCardNumber() {
        return recipientCardNumber;
    }

    public void setRecipientCardNumber(String recipientCardNumber) {
        this.recipientCardNumber = recipientCardNumber;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(String operationStatus) {
        this.operationStatus = operationStatus;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.operationTime);
        dest.writeString(this.receiptNumber);
        dest.writeString(this.sendCardNumber);
        dest.writeString(this.recipientName);
        dest.writeString(this.recipientCardNumber);
        dest.writeString(this.fee);
        dest.writeString(this.amount);
        dest.writeString(this.operationStatus);
        dest.writeString(this.serviceName);
        dest.writeString(this.accountNumber);
        dest.writeInt(this.type);
    }

    @Override
    public String toString() {
        return "ReceiptModel{" +
                "operationTime='" + operationTime + '\'' +
                ", receiptNumber='" + receiptNumber + '\'' +
                ", sendCardNumber='" + sendCardNumber + '\'' +
                ", recipientName='" + recipientName + '\'' +
                ", recipientCardNumber='" + recipientCardNumber + '\'' +
                ", fee='" + fee + '\'' +
                ", amount='" + amount + '\'' +
                ", operationStatus='" + operationStatus + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                '}';
    }
}