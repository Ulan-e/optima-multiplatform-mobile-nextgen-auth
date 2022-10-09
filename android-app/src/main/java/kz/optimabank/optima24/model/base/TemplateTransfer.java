package kz.optimabank.optima24.model.base;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class TemplateTransfer extends Templates implements Serializable {
    @SerializedName("ContragentAddress")
    public String ContragentAddress;
    @SerializedName("ContragentCountry")
    public String ContragentCountry;
    @SerializedName("ContragentRegistrationCountry")
    public String ContragentRegistrationCountry;
    @SerializedName("IntermediaryName")
    public String IntermediaryName;
    @SerializedName("IntermediaryBic")
    public String IntermediaryBic;
    @SerializedName("PayerName")
    public String PayerName;
    @SerializedName("IntermediaryAccountNumber")
    public String IntermediaryAccountNumber;
    @SerializedName("IntermediaryAccount")
    public String IntermediaryAccount;
    @SerializedName("ChargesType")
    public String ChargesType;
    @SerializedName("ContragentKpp")
    public String ContragentKpp;
    @SerializedName("OperationVoCode")
    public String OperationVoCode;
    @SerializedName("ContragentBicAccount")
    public String ContragentBicAccount;
    @SerializedName("FeeAccountId")
    public int FeeAccountId;
    @SerializedName("PayerAddress")
    public String PayerAddress;
    @SerializedName("PayerCity")
    public String PayerCity;
    @SerializedName("PayerCountry")
    public String PayerCountry;
    @SerializedName("FundTarget")
    public String FundTarget;
    @SerializedName("RecipientRelation")
    public String RecipientRelation;
    @SerializedName("Name")
    public String Name;
    @SerializedName("ContragentAccountNumber")
    public String ContragentAccountNumber;
    @SerializedName("ContragentBankAccountNumber")
    public String ContragentBankAccountNumber;
    @SerializedName("ContragentBic")
    public String ContragentBic;
    @SerializedName("ContragentBicName")
    public String ContragentBicName;
    @SerializedName("ContragentIdn")
    public String ContragentIdn;
    @SerializedName("ContragentName")
    public String ContragentName;
    @SerializedName("OperationKnp")
    public String OperationKnp;
    @SerializedName("OperationPurpose")
    public String OperationPurpose;
    @SerializedName("Id")
    public int Id;
    @SerializedName("DestinationAccountId")
    public int DestinationAccountId;
    @SerializedName("SourceAccountId")
    public int SourceAccountId;
    @SerializedName("ProductType")
    public int ProductType;
    @SerializedName("TransferType")
    public int TransferType;
    @SerializedName("UserId")
    public int UserId;
    @SerializedName("ContragentResidency")
    public int ContragentResidency;
    @SerializedName("ContragentSeco")
    public int ContragentSeco;
    @SerializedName("Amount")
    public double Amount;
    @SerializedName("StandingInstructionTime")
    public String StandingInstructionTime;
    @SerializedName("StandingInstructionDate")
    public String StandingInstructionDate;
    @SerializedName("StandingInstructionType")
    public int StandingInstructionType;
    @SerializedName("AutoTransferCount")
    public int AutoTransferCount;
    @SerializedName("StandingInstructionStatus")
    public int StandingInstructionStatus;
    @SerializedName("StandingInstruction")
    public boolean StandingInstruction;
    @SerializedName("AutoTransferDateBegin")
    public String AutoTransferDateBegin;
    @SerializedName("Currency")
    public String Currency;
    @SerializedName("ContragentCardBrandType")
    public String ContragentCardBrandType;
    @SerializedName("StartPayDay")
    public String StartPayDay;
    @SerializedName("ConfirmCode")
    public String ConfirmCode;
    @SerializedName("OperationCode")
    public String operationCode;
    @SerializedName("RequestId")
    public Long requestId;

    public void setName(String name) {
        Name = name;
    }

    public void setContragentAccountNumber(String contragentAccountNumber) {
        ContragentAccountNumber = contragentAccountNumber;
    }


    public void setContragentBic(String contragentBic) {
        ContragentBic = contragentBic;
    }

    public void setContragentIdn(String contragentIdn) {
        ContragentIdn = contragentIdn;
    }

    public void setContragentName(String contragentName) {
        ContragentName = contragentName;
    }

    public void setOperationKnp(String operationKnp) {
        OperationKnp = operationKnp;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setDestinationAccountId(int destinationAccountId) {
        DestinationAccountId = destinationAccountId;
    }

    public void setSourceAccountId(int sourceAccountId) {
        SourceAccountId = sourceAccountId;
    }

    public void setProductType(int productType) {
        ProductType = productType;
    }

    public void setTransferType(int transferType) {
        TransferType = transferType;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public void setContragentResidency(int contragentResidency) {
        ContragentResidency = contragentResidency;
    }

    public void setContragentSeco(int contragentSeco) {
        ContragentSeco = contragentSeco;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public void setStandingInstructionTime(String standingInstructionTime) {
        StandingInstructionTime = standingInstructionTime;
    }

    public void setStandingInstructionDate(String standingInstructionDate) {
        StandingInstructionDate = standingInstructionDate;
    }

    public void setStandingInstructionType(int standingInstructionType) {
        StandingInstructionType = standingInstructionType;
    }

    public void setStandingInstructionStatus(int standingInstructionStatus) {
        StandingInstructionStatus = standingInstructionStatus;
    }

    public void setStandingInstruction(boolean standingInstruction) {
        StandingInstruction = standingInstruction;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }


    public void setContragentCardBrandType(String contragentCardBrandType) {
        ContragentCardBrandType = contragentCardBrandType;
    }


    public void setStartPayDay(String startPayDay) {
        StartPayDay = startPayDay;
    }


    public void setConfirmCode(String confirmCode) {
        this.ConfirmCode = confirmCode;
    }

    public String getCurrency() {
        return Currency;
    }

    public boolean isStandingInstruction() {
        return StandingInstruction;
    }

    public int getStandingInstructionStatus() {
        return StandingInstructionStatus;
    }

    public int getStandingInstructionType() {
        return StandingInstructionType;
    }

    public void setOperationPurpose(String operationPurpose) {
        OperationPurpose = operationPurpose;
    }

    public String getName() {
        return Name;
    }

    public String getContragentAccountNumber() {
        return ContragentAccountNumber;
    }

    public String getContragentBankAccountNumber() {
        return ContragentBankAccountNumber;
    }

    public int getTransferType() {
        return TransferType;
    }

    public String getContragentBic() {
        return ContragentBic;
    }

    public String getContragentBicName() {
        return ContragentBicName;
    }

    public String getOperationPurpose() {
        return OperationPurpose;
    }

    public String getContragentIdn() {
        return ContragentIdn;
    }

    public String getContragentName() {
        return ContragentName;
    }

    public String getOperationKnp() {
        return OperationKnp;
    }

    public int getId() {
        return Id;
    }

    public int getContragentResidency() {
        return ContragentResidency;
    }

    public String getAmount() {
        BigDecimal bd = new BigDecimal(Amount);
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');
        DecimalFormat formatter = new DecimalFormat("###,##0.00", symbols);
        return formatter.format(bd.doubleValue()) + " ";
    }

    public int getDestinationAccountId() {
        return DestinationAccountId;
    }

    public int getSourceAccountId() {
        return SourceAccountId;
    }

    public int getProductType() {
        return ProductType;
    }

    public int getUserId() {
        return UserId;
    }

    public String getContragentCountry() {
        return ContragentCountry;
    }

    public String getContragentRegistrationCountry() {
        return ContragentRegistrationCountry;
    }

    public String getContragentAddress() {
        return ContragentAddress;
    }

    public String getContragentKpp() {
        return ContragentKpp;
    }

    public String getIntermediaryName() {
        return IntermediaryName;
    }

    public String getIntermediaryBic() {
        return IntermediaryBic;
    }


}