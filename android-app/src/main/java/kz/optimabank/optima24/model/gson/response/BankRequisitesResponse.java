package kz.optimabank.optima24.model.gson.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BankRequisitesResponse implements Serializable {

    @SerializedName("data")
    private Data data;
    @SerializedName("success")
    private Boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("code")
    private Integer code;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public static class Data extends BankRequisitesResponse implements Serializable {
        @SerializedName("Id")
        private Integer id;
        @SerializedName("BranchCode")
        private String branchCode;
        @SerializedName("BranchName")
        private String branchName;
        @SerializedName("Addresss")
        private String addresss;
        @SerializedName("Bic")
        private String bic;
        @SerializedName("Currency")
        private String currency;
        @SerializedName("CurrencyCode")
        private String currencyCode;
        @SerializedName("Account")
        private String account;
        @SerializedName("RequisitesSwiftTransfer")
        private List<RequisitesSwiftTransfer> requisitesSwiftTransfer;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getBranchCode() {
            return branchCode;
        }

        public void setBranchCode(String branchCode) {
            this.branchCode = branchCode;
        }

        public String getBranchName() {
            return branchName;
        }

        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }

        public String getAddresss() {
            return addresss;
        }

        public void setAddresss(String addresss) {
            this.addresss = addresss;
        }

        public String getBic() {
            return bic;
        }

        public void setBic(String bic) {
            this.bic = bic;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public List<RequisitesSwiftTransfer> getRequisitesSwiftTransfer() {
            return requisitesSwiftTransfer;
        }

        public void setRequisitesSwiftTransfer(List<RequisitesSwiftTransfer> requisitesSwiftTransfer) {
            this.requisitesSwiftTransfer = requisitesSwiftTransfer;
        }

    }

    public static class RequisitesSwiftTransfer extends BankRequisitesResponse implements Serializable {

        @SerializedName("Id")
        private Integer id;
        @SerializedName("CurrencyCode")
        private String currencyCode;
        @SerializedName("IntermediaryBank")
        private String intermediaryBank;
        @SerializedName("BeneficiaryBank")
        private String beneficiaryBank;
        @SerializedName("Beneficiary")
        private String beneficiary;
        @SerializedName("DetailsOfPayment")
        private String detailOfPayments;

        public String getDetailOfPayments() {
            return detailOfPayments;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getIntermediaryBank() {
            return intermediaryBank;
        }

        public void setIntermediaryBank(String intermediaryBank) {
            this.intermediaryBank = intermediaryBank;
        }

        public String getBeneficiaryBank() {
            return beneficiaryBank;
        }

        public void setBeneficiaryBank(String beneficiaryBank) {
            this.beneficiaryBank = beneficiaryBank;
        }

        public String getBeneficiary() {
            return beneficiary;
        }

        public void setBeneficiary(String beneficiary) {
            this.beneficiary = beneficiary;
        }

    }
}
