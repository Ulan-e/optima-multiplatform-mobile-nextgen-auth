package kz.optimabank.optima24.model.base;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


public class PenaltyItem implements Serializable {
    private static final long serialVersionUID = 1647444584469131523L;
    @SerializedName("BeneficiaryBIN")
    public String BeneficiaryBIN;
    @SerializedName("BeneficiaryBank")
    public String BeneficiaryBank;
    @SerializedName("BeneficiaryDepartmentName")
    public String BeneficiaryDepartmentName;
    @SerializedName("BeneficiaryName")
    public String BeneficiaryName;
    @SerializedName("BeneficiaryRNN")
    public String BeneficiaryRNN;
    @SerializedName("BlankDate")
    public String BlankDate;
    @SerializedName("BlankNumber")
    public String BlankNumber;
    @SerializedName("BlankSerial")
    public String BlankSerial;
    @SerializedName("BlankType")
    public String BlankType;
    @SerializedName("BlankTypeName")
    public String BlankTypeName;
    @SerializedName("CarGovernNumber")
    public String CarGovernNumber;
    @SerializedName("DriverLicenseNumber")
    public String DriverLicenseNumber;
    @SerializedName("FixSum")
    public String FixSum;
    @SerializedName("Id")
    public String Id;
    @SerializedName("PaySum")
    public String PaySum;
    @SerializedName("PayerIdn")
    public String PayerIdn;
    @SerializedName("PayerName")
    public String PayerName;
    @SerializedName("TaxKbk")
    public String TaxKbk;
    @SerializedName("TaxKbkName")
    public String TaxKbkName;
    @SerializedName("TaxKno")
    public String TaxKno;
    @SerializedName("TaxKnp")
    public String TaxKnp;
    @SerializedName("TaxKnpName")
    public String TaxKnpName;
    @SerializedName("TechPassportNumber")
    public String TechPassportNumber;
    @SerializedName("ViolationDate")
    public String ViolationDate;
    @SerializedName("ViolationName")
    public String ViolationName;
    @SerializedName("ViolationPlace")
    public String ViolationPlace;
    @SerializedName("ViolationPunishment")
    public String ViolationPunishment;
    @SerializedName("XmlInvoice")
    public String XmlInvoice;
    @SerializedName("PaymentParameters")
    public ArrayList<PaymentParameter> paymentParameters;


    public void setBeneficiaryBIN(String beneficiaryBIN) {
        BeneficiaryBIN = beneficiaryBIN;
    }

    public void setBeneficiaryDepartmentName(String beneficiaryDepartmentName) {
        BeneficiaryDepartmentName = beneficiaryDepartmentName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        BeneficiaryName = beneficiaryName;
    }

    public void setBeneficiaryRNN(String beneficiaryRNN) {
        BeneficiaryRNN = beneficiaryRNN;
    }

    public void setBlankDate(String blankDate) {
        BlankDate = blankDate;
    }

    public void setBlankNumber(String blankNumber) {
        BlankNumber = blankNumber;
    }

    public void setBlankSerial(String blankSerial) {
        BlankSerial = blankSerial;
    }

    public void setBlankType(String blankType) {
        BlankType = blankType;
    }

    public void setBlankTypeName(String blankTypeName) {
        BlankTypeName = blankTypeName;
    }

    public void setCarGovernNumber(String carGovernNumber) {
        CarGovernNumber = carGovernNumber;
    }

    public void setDriverLicenseNumber(String driverLicenseNumber) {
        DriverLicenseNumber = driverLicenseNumber;
    }

    public void setFixSum(String fixSum) {
        FixSum = fixSum;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setPayerIdn(String payerIdn) {
        PayerIdn = payerIdn;
    }

    public void setPayerName(String payerName) {
        PayerName = payerName;
    }

    public void setPaySum(String paySum) {
        PaySum = paySum;
    }

    public static class PaymentParameter {
        private static final long serialVersionUID = 1647444584469131543L;
        @SerializedName("Label")
        public String Label;
        @SerializedName("Name")
        public String Name;
        @SerializedName("ParameterId")
        public String ParameterId;
        @SerializedName("Value")
        public String Value;
    }
}
