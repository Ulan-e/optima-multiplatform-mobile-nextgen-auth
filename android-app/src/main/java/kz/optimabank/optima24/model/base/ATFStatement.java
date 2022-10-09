package kz.optimabank.optima24.model.base;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import kz.optimabank.optima24.utility.Constants;

public class ATFStatement implements Cloneable{
    @SerializedName("Id")
    public int id;
    @SerializedName("Currency")
    public String currency;
    @SerializedName("ClientId")
    public int ClientId;
    @SerializedName("Name")
    public String name;
    @SerializedName("OperationDate")
    public String operationDate;
    @SerializedName("Amount")
    public float amount;
    @SerializedName("BaseAmount")
    public float baseAmount;
    @SerializedName("BaseCurrency")
    public String baseCurrency;
    @SerializedName("ContragentNumberInfo")
    public String contragentNumberInfo;
//    @SerializedName("OperationReference")
//    public String reference;
    @SerializedName("Description")
    public String description;
    @SerializedName("Fee")
    public double fee;
    @SerializedName("CategoryId")
    public double categoryId;
    @SerializedName("TAmount")
    public double tAmount;
    @SerializedName("TCurrency")
    public String tCurrency ;

    public ATFStatement(int id) {
        this.id = id;
    }

    public ATFStatement(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getOperationDate() {
        String stDate = null;
        SimpleDateFormat sdf = getSimpleDateFormat();
        try {
            stDate = Constants.VIEW_DATE_FORMAT.format(sdf.parse(operationDate));
            TimeUnit.MILLISECONDS.toDays(sdf.parse(operationDate).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stDate;
    }

    public String getOperationTime() {
        String stDate = null;
        SimpleDateFormat sdf = getSimpleDateFormat();
        try {
            stDate = Constants.API_TIME_FORMAT.format(sdf.parse(operationDate));
            TimeUnit.MILLISECONDS.toDays(sdf.parse(operationDate).getTime());
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

    public ATFStatement clone() throws CloneNotSupportedException {
        return (ATFStatement)super.clone();
    }

    public String getFormattedTAmount(Context context) {
        BigDecimal bd = new BigDecimal(tAmount);
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');
        DecimalFormat formatter = new DecimalFormat("###,##0.00", symbols);
//        return formatter.format(bd.doubleValue()) + " " + getCurrencyBadge(context, currency);
        return formatter.format(bd.doubleValue()) + " " + tCurrency;
    }

    public String getFormattedAmount(Context context) {
        BigDecimal bd = new BigDecimal(amount);
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');
        DecimalFormat formatter = new DecimalFormat("###,##0.00", symbols);
        return formatter.format(bd.doubleValue()) + " " + currency;
    }

    public String getFormattedFee(Context context) {
        BigDecimal bd = new BigDecimal(fee);
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');
        DecimalFormat formatter = new DecimalFormat("###,##0.00", symbols);
//        return formatter.format(bd.doubleValue()) + " " + getCurrencyBadge(context, currency);
        return formatter.format(bd.doubleValue()) + " " + currency;
    }

    public String getCurrency() {
        return currency;
    }

//    public String getReference() {
//        return reference;
//    }
//    public String getFormattedAmount() {
//        return "";
//    }

    public String getDescription() {
        return "";
    }


//    public static class CardAccountStatement {
//        @SerializedName("card_operations")
//        private ArrayList<CardStatement> card_operations;
//
//        public ArrayList<CardStatement> getOperations() {
//            return card_operations;
//        }
//    }
//
//    public static class CurrentAccountStatement {
//        @SerializedName("account_operations")
//        private ArrayList<AccountStatement> account_operations;
//
//        public ArrayList<AccountStatement> getOperations() {
//            return account_operations;
//        }
//
//    }
//
//    public static class AccountStatement extends ATFStatement {
//        @SerializedName("operation_date")
//        private Date operation_date;
//        @SerializedName("operation_amount")
//        private double operation_amount;
//        @SerializedName("description")
//        private String description;
//
//
//        public AccountStatement() {
//
//        }
//
//        public double getAmount() {
//            return operation_amount;
//        }
//
//        @Override
//        public String getDescription() {
//            return description;
//        }
//
//
//
//
//        @Override
//        public String getFormattedAmount() {
//            BigDecimal bd = new BigDecimal(operation_amount);
//            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
//            symbols.setGroupingSeparator(' ');
//            DecimalFormat formatter = new DecimalFormat("###,##0.00", symbols);
//            return formatter.format(bd.doubleValue()) + " " + Currency;
//        }
//
//        public String getFormattedDate() {
//            return Constants.API_DATE_FORMAT.format(operation_date);
//        }
//    }
//
//    public static class CardStatement extends ATFStatement {
//        @SerializedName("amount")
//        public double amount;
//        @SerializedName("operation_date")
//        private Date operation_date;
//        @SerializedName("operation_amount")
//        private double operation_amount;
//        @SerializedName("fee")
//        private double fee;
//        @SerializedName("description")
//        private String description;
//
//        public CardStatement() {
//
//        }
//
//        public Date getDate() {
//            return operation_date;
//        }
//
//        public double getAmount() {
//            return operation_amount;
//        }
//
//        public double getFee() {
//            return fee;
//        }
//
//        public String getFormattedDate() {
//            return Constants.API_DATE_FORMAT.format(operation_date);
//        }
//
//        @Override
//        public String getDescription() {
//            return description;
//        }
//
//        @Override
//        public String getFormattedAmount() {
//            BigDecimal bd = new BigDecimal(operation_amount);
//            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
//            symbols.setGroupingSeparator(' ');
//            DecimalFormat formatter = new DecimalFormat("###,##0.00", symbols);
//            if (operation_amount != amount) {
//                return formatter.format(bd.doubleValue()) + " " + Currency + "\n" + formatter.format(new BigDecimal(amount));
//            }
//            return formatter.format(bd.doubleValue()) + " " + Currency;
////            return String.format("%.2f %s", operation_amount, Currency);
//        }
//
//    }
}
