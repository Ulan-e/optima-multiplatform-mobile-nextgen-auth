package kz.optimabank.optima24.model.gson.response;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.utility.Constants;

/**
  Created by Timur on 01.03.2017.
 */

public class UserAccounts implements Serializable {
    @SerializedName("Code")
    public int code;
    @SerializedName("AccountType")
    public int accountType;
    @SerializedName("Number")
    public String number;
    @SerializedName("Currency")
    public String currency;
    @SerializedName("Balance")
    public double balance;
    @SerializedName("Name")
    public String name;
    @SerializedName("IsBlocked")
    public boolean isBlocked;
    @SerializedName("IsVisible")
    public boolean isVisible;
    @SerializedName("IsClosed")
    public boolean isClosed;
    @SerializedName("IsDebit")
    public boolean isDebit;
    @SerializedName("IsCredit")
    public boolean isCredit;
    @SerializedName("IsBlockedFromOptima24")
    public boolean isBlockedFromOptima24;
    @SerializedName("IsMultibalance")
    public boolean isMultiBalance;

    private boolean isVisibility;

    public UserAccounts() {
    }

    public UserAccounts(int code) {
        this.code = code;
    }

    public UserAccounts(String name) {
        this.code = Constants.HEADER_ID;
        this.name = name;
    }
    public UserAccounts(String name, int code) {
        this.code = code;
        this.name = name;
    }

    public String getFormattedBalance(Context context) {
        BigDecimal bd = new BigDecimal(balance);
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');
        DecimalFormat formatter = new DecimalFormat("###,##0.00", symbols);
        return formatter.format(bd.doubleValue()) + " " + currency;
    }

    public boolean isVisibility() {
        return isVisibility;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public void setVisibility(boolean isVisibility) {
        this.isVisibility = isVisibility;
    }

    public static class Cards extends UserAccounts {
        @SerializedName("BlockedAmount")
        public double blockedAmount;
        @SerializedName("DebtAmount")
        public double debtAmount;
        @SerializedName("MinPayAmount")
        public double minPayAmount;
        @SerializedName("CardholderName")
        public String cardholderName;
        @SerializedName("Brand")
        public String brand;
        @SerializedName("BrandType")
        public int brandType;
        @SerializedName("ProductName")
        public String productName;
        @SerializedName("CreditLimitAmount")
        public double creditLimitAmount;
        @SerializedName("CardAccountCode")
        public int cardAccountCode;
        @SerializedName("ExpireDate")
        public String expireDate;
        @SerializedName("RbsNumber")
        public String rbsNumber;
        @SerializedName("MultiBalanceList")
        public ArrayList<MultiBalanceList> multiBalanceList;
        @SerializedName("CardImage")
        public CardImage imageList;
        @SerializedName("EncryptPossibility")
        public boolean encryptPossibility;
        @SerializedName("DefaultStatus")
        public int defaultStatus;

        public int getDefaultStatus() {
            return defaultStatus;
        }

        @SerializedName("Id")
        public long id;

        public long getId() {
            return id;
        }

        public byte[] fullImStr;
        //@SerializedName("miniatureIm")
        //public transient Bitmap miniatureIm;
        public byte[] miniatureImStr;

        public class CardImage implements Serializable {
            @SerializedName("Miniature")
            public String miniature;
            @SerializedName("Full")
            public String full;
        }

        public byte[] getByteArrayFullImg () {
            //Log.i("byteArrayFullImg","UA fullImStr = "+fullImStr);
            /*if (fullImStr != null) {
                //Log.i("byteArrayFullImg","UA Base64.decode(fullImStr, Base64.DEFAULT = "+ Arrays.toString(Base64.decode(fullImStr, Base64.DEFAULT)));
                return fullImStr;
                //return Base64.decode(fullImStr, Base64.DEFAULT);
            }*/
            //return null;
            try {
                return GeneralManager.getInstance().fullBimapForCards.get(code);
            }catch (Exception ignored){
                return null;
            }
        }

        public byte[] getByteArrayMiniatureImg () {
            /*if (miniatureImStr != null) {

                return miniatureImStr;
                //return Base64.decode(miniatureImStr, Base64.DEFAULT);
            }*/
            //return null;
            try {
                return GeneralManager.getInstance().minBimapForCards.get(code);
            }catch (Exception ignored){
                return null;
            }
        }

        public class MultiBalanceList implements Serializable {
            public int code;
            public int name;

            public MultiBalanceList(int code, int name) {
                this.code = code;
                this.name = name;
            }

            @SerializedName("Amount")
            public double amount;
            @SerializedName("Currency")
            public String currency;

            public String getFormattedAmount(Context context) {
                BigDecimal bd = new BigDecimal(amount);
                DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
                symbols.setGroupingSeparator(' ');
                DecimalFormat formatter = new DecimalFormat("###,##0.00", symbols);
//                return formatter.format(bd.doubleValue()) + " " + getCurrencyBadge(context,currency);
                return formatter.format(bd.doubleValue()) + " " + currency;
            }
        }

        public Date getCardExpireDate() {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            sdf.setTimeZone(TimeZone.getDefault());
            try {
                return sdf.parse(expireDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public String getFormattedSum(Context context,double sum) {
            BigDecimal bd = new BigDecimal(sum);
            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
            symbols.setGroupingSeparator(' ');
            DecimalFormat formatter = new DecimalFormat("###,##0.00", symbols);
//        return String.format("%.2f %s", balance, Currency);
//            return formatter.format(bd.doubleValue()) + " " + getCurrencyBadge(context,currency);
            return formatter.format(bd.doubleValue()) + " " + currency;
        }
    }

    public class EncryptedCard extends UserAccounts {
        @SerializedName("ClientId")
        public int clientId;
        @SerializedName("BankId")
        public String bankId;
        @SerializedName("Bic")
        public String bic;
        @SerializedName("Enabled")
        public boolean enabled;
    }

    public class CardAccounts extends UserAccounts {
        @SerializedName("AccountOwner")
        public String accountOwner;
        @SerializedName("BlockedAmount")
        public double blockedAmount;
        @SerializedName("TotalBalance")
        public double totalBalance;
        @SerializedName("CreditLimitAmount")
        public double creditLimitAmount;
        @SerializedName("ProductScheme")
        public String productScheme;


        public class Cards extends UserAccounts.Cards{}
    }

    public class CheckingAccounts extends UserAccounts{
        @SerializedName("Id")
        public String id;
    }

    public class CreditAccounts extends UserAccounts {
        @SerializedName("AgreementNumber")
        public String agreementNumber;
        @SerializedName("AgreementAmount")
        public double agreementAmount;
        @SerializedName("AgreementDate")
        public String agreementDate;
        @SerializedName("AgreementEndDate")
        public String agreementEndDate;
        @SerializedName("InterestRate")
        public String interestRate;
        @SerializedName("NextPaymentDate")
        public String nextPaymentDate;
        @SerializedName("NextPaymentAmount")
        public double nextPaymentAmount;

        public String getFormattedBalance(Context context) {
            BigDecimal bd = new BigDecimal(agreementAmount - balance);
            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
            symbols.setGroupingSeparator(' ');
            DecimalFormat formatter = new DecimalFormat("###,##0.00", symbols);
//        return String.format("%.2f %s", balance, Currency);
//            return formatter.format(bd.doubleValue()) + " " + getCurrencyBadge(context,currency);
            return formatter.format(bd.doubleValue()) + " " + currency;
        }

        public String getFormattedNextPaymentAmount() {
            BigDecimal bd = new BigDecimal(nextPaymentAmount);
            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
            symbols.setGroupingSeparator(' ');
            DecimalFormat formatter = new DecimalFormat("###,##0.00", symbols);
            return formatter.format(bd.doubleValue()) + " " + currency;
        }

        public String getNextPaymentDate() {
            String stDate = null;
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            sdf.setTimeZone(TimeZone.getDefault());
            try {
                if(nextPaymentDate!=null) {
                    stDate = Constants.VIEW_DATE_FORMAT.format(sdf.parse(nextPaymentDate));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return stDate;
        }

        public String getAgreementDate() {
            String stDate = null;
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setTimeZone(TimeZone.getDefault());
            try {
                if(agreementDate!=null) {
                    stDate = Constants.VIEW_DATE_FORMAT.format(sdf.parse(agreementDate));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return stDate;
        }

        public String getNextPaymentDay() {
            String stDate = null;
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            sdf.setTimeZone(TimeZone.getDefault());
            try {
                if(nextPaymentDate!=null) {
                    stDate = Constants.DAY_DATE_FORMAT.format(sdf.parse(nextPaymentDate));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return stDate;
        }
    }

    public class DepositAccounts extends UserAccounts {
        @SerializedName("AgreementNumber")
        public String agreementNumber;
        @SerializedName("ProductName")
        public String productName;
        @SerializedName("Rate")
        public double rate;
        @SerializedName("Period")
        public int Period;
        @SerializedName("Capitalization")
        public boolean capitalization;
        @SerializedName("MinBalance")
        public double minBalance;
        @SerializedName("OpenDate")
        public String openDate;
        @SerializedName("ExpireDate")
        public String expireDate;
        @SerializedName("IsWithDrawal")
        public boolean isWithDrawal;
        @SerializedName("InterestTargetIban ")
        public int interestTargetIban;
        @SerializedName("IsWish")
        public boolean isWish;
        @SerializedName("ProductCode")
        public int productCode;

        public String getDepositExpireDate() {
            String stDate = null;
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            sdf.setTimeZone(TimeZone.getDefault());
            try {
                stDate = Constants.DAY_MONTH_YEAR_FORMAT.format(sdf.parse(expireDate));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return stDate;
        }

        public String getDepositOpenDate() {
            String stDate = null;
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            sdf.setTimeZone(TimeZone.getDefault());
            try {
                stDate = Constants.DAY_MONTH_YEAR_FORMAT.format(sdf.parse(openDate));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return stDate;
        }
    }

    public class WishAccounts extends DepositAccounts {
        @SerializedName("MountlyPayment")
        public double mountlyPayment;
        @SerializedName("WishName")
        public String wishName;
        @SerializedName("TotalAmount")
        public double totalAmount;
        @SerializedName("PictureName")
        public String pictureName;
        @SerializedName("AccountCode")
        public int accountCode;
    }
}
