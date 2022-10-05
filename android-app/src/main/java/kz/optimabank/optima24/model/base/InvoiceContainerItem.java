package kz.optimabank.optima24.model.base;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import kz.optimabank.optima24.utility.Constants;

public class InvoiceContainerItem implements Serializable {
    @SerializedName("CalcParams")
    public ArrayList<CalcParams> calcParams;
    @SerializedName("OwnerInfo")
    public OwnerInfo ownerInfo;
    @SerializedName("InvoiceBody")
    public InvoiceBody invoiceBody;

//    private String name;
//
//    public InvoiceContainerItem(String name) {
//        this.name = name;
//    }

    public String getDate(String date) {
        String stDate = null;
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getDefault());
        try {
            stDate = Constants.VIEW_DATE_FORMAT.format(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return stDate;
    }

    public class CalcParams implements Serializable {
        @SerializedName("Id")
        public String CalcParamsId;
        @SerializedName("Name")
        public String CalcParamsName;
        @SerializedName("Value")
        public String CalcParamsValue;
    }

    public class OwnerInfo implements Serializable {
        @SerializedName("Id")
        public String Id;
        @SerializedName("Name")
        public String Name;
        @SerializedName("Address")
        public String Address;

        public String getId() {
            return Id;
        }
    }

    public class InvoiceBody implements Serializable {
        @SerializedName("OccupantsCount")
        public double OccupantsCount;
        @SerializedName("Id")
        public String Id;
        @SerializedName("Items")
        private ArrayList<BodyItem> items;
        @SerializedName("ExpireDate")
        public String ExpireDate;
        @SerializedName("FormedDate")
        public String FormedDate;
        @SerializedName("Currency")
        public String Currency;

        public ArrayList<BodyItem> getItems() {
            return items;
        }
    }

    public static class BodyItem implements Serializable {
        boolean useDebt;
        boolean notUseDebtForFixInvoice;
        boolean usePenalty;
        private boolean isPay = true;
        double serviceSum = -1000;

        public BodyItem(String serviceName) {
            ServiceName = serviceName;
        }

        @SerializedName("AvgCount")
        private String AvgCount;
        @SerializedName("AvgPaySum")
        private String AvgPaySum;
        @SerializedName("Calc")
        private String Calc;
        @SerializedName("DebtInfo")
        private double DebtInfo;
        @SerializedName("ExpireDateInfo")
        private String ExpireDateInfo;
        @SerializedName("FixCount")
        private double FixCount;
        @SerializedName("FixSum")
        private double FixSum;
        @SerializedName("IsComplexCalculations")
        public boolean IsComplexCalculations;
        @SerializedName("IsCounterService")
        private boolean IsCounterService;
        @SerializedName("LastCount")
        private double LastCount;
        @SerializedName("LastCountDate")
        private String LastCountDate;
        @SerializedName("Losses")
        private String Losses;
        @SerializedName("MaxTariffValue")
        private double MaxTariffValue;
        @SerializedName("Measure")
        private String Measure;
        @SerializedName("MiddleTariffThreshold")
        private double MiddleTariffThreshold;
        @SerializedName("MiddleTariffValue")
        private double MiddleTariffValue;
        @SerializedName("MinTariffThreshold")
        private double MinTariffThreshold;
        @SerializedName("MinTariffValue")
        private double MinTariffValue;
        @SerializedName("PaySum")
        private String PaySum;
        @SerializedName("Penalty")
        private double Penalty;
        @SerializedName("PrevCount")
        private double PrevCount;
        @SerializedName("PrevCountDate")
        private String PrevCountDate;
        @SerializedName("ServiceId")
        private int ServiceId;
        @SerializedName("ServiceName")
        private String ServiceName;
        @SerializedName("Transformation")
        private String Transformation;

        @SerializedName("IsEditable")
        private boolean IsEditable;

        public boolean isEditable() {
            return IsEditable;
        }

        public String getAvgCount() {
            return AvgCount;
        }

        public void setAvgCount(String avgCount) {
            AvgCount = avgCount;
        }

        public String getAvgPaySum() {
            return AvgPaySum;
        }

        public void setAvgPaySum(String avgPaySum) {
            AvgPaySum = avgPaySum;
        }

        public String getCalc() {
            return Calc;
        }

        public void setCalc(String calc) {
            Calc = calc;
        }

        public double getDebtInfo() {
            return DebtInfo;
        }

        public void setDebtInfo(double debtInfo) {
            DebtInfo = debtInfo;
        }

        public String getExpireDateInfo() {
            return ExpireDateInfo;
        }

        public void setExpireDateInfo(String expireDateInfo) {
            ExpireDateInfo = expireDateInfo;
        }

        public double getFixCount() {
            return FixCount;
        }

        public void setFixCount(double fixCount) {
            FixCount = fixCount;
        }

        public double getFixSum() {
            return FixSum;
        }

        public void setFixSum(double fixSum) {
            FixSum = fixSum;
        }

        public boolean isComplexCalculations() {
            return IsComplexCalculations;
        }

        public void isPay(boolean isPay) {
            this.isPay = isPay;
            Log.i("isPay","isPay = "+isPay);
        }

        public boolean isPay() {
            return isPay;
        }

        public void setComplexCalculations(boolean complexCalculations) {
            IsComplexCalculations = complexCalculations;
        }

        public boolean isCounterService() {
            return IsCounterService;
        }

        public void setCounterService(boolean counterService) {
            IsCounterService = counterService;
        }

        public double getLastCount() {
            return LastCount;
        }

        public void setLastCount(double lastCount) {
            LastCount = lastCount;
        }

        public String getLastCountDate() {
            return LastCountDate;
        }

        public void setLastCountDate(String lastCountDate) {
            LastCountDate = lastCountDate;
        }

        public String getLosses() {
            return Losses;
        }

        public void setLosses(String losses) {
            Losses = losses;
        }

        public double getMaxTariffValue() {
            return MaxTariffValue;
        }

        public void setMaxTariffValue(double maxTariffValue) {
            MaxTariffValue = maxTariffValue;
        }

        public String getMeasure() {
            return Measure;
        }

        public void setMeasure(String measure) {
            Measure = measure;
        }

        public double getMiddleTariffThreshold() {
            return MiddleTariffThreshold;
        }

        public void setMiddleTariffThreshold(double middleTariffThreshold) {
            MiddleTariffThreshold = middleTariffThreshold;
        }

        public double getMiddleTariffValue() {
            return MiddleTariffValue;
        }

        public void setMiddleTariffValue(double middleTariffValue) {
            MiddleTariffValue = middleTariffValue;
        }

        public double getMinTariffThreshold() {
            return MinTariffThreshold;
        }

        public void setMinTariffThreshold(double minTariffThreshold) {
            MinTariffThreshold = minTariffThreshold;
        }

        public double getMinTariffValue() {
            return MinTariffValue;
        }

        public void setMinTariffValue(double minTariffValue) {
            MinTariffValue = minTariffValue;
        }

        public String getPaySum() {
            return PaySum;
        }

        public void setPaySum(String paySum) {
            PaySum = paySum;
        }

        public double getPenalty() {
            return Penalty;
        }

        public void setPenalty(double penalty) {
            Penalty = penalty;
        }

        public double getPrevCount() {
            return PrevCount;
        }

        public void setPrevCount(double prevCount) {
            PrevCount = prevCount;
        }

        public String getPrevCountDate() {
            return PrevCountDate;
        }

        public void setPrevCountDate(String prevCountDate) {
            PrevCountDate = prevCountDate;
        }

        public int getServiceId() {
            return ServiceId;
        }

        public void setServiceId(int serviceId) {
            ServiceId = serviceId;
        }

        public String getServiceName() {
            return ServiceName;
        }

        public void setServiceName(String serviceName) {
            ServiceName = serviceName;
        }

        public String getTransformation() {
            return Transformation;
        }

        public void setTransformation(String transformation) {
            Transformation = transformation;
        }

        public boolean isUseDebt() {
            return useDebt;
        }

        public void setUseDebt(boolean useDebt) {
            this.useDebt = useDebt;
        }

        public boolean isUsePenalty() {
            return usePenalty;
        }

        public void setUsePenalty(boolean usePenalty) {
            this.usePenalty = usePenalty;
        }

        public double getServiceSum() {
            return serviceSum;
        }

        public void setServiceSum(double serviceSum) {
            this.serviceSum = serviceSum;
        }

        public boolean isNotUseDebtForFixInvoice() {
            return notUseDebtForFixInvoice;
        }

        public void setNotUseDebtForFixInvoice(boolean notUseDebtForFixInvoice) {
            this.notUseDebtForFixInvoice = notUseDebtForFixInvoice;
        }
    }
}

