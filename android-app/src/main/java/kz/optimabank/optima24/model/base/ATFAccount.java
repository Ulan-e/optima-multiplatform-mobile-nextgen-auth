package kz.optimabank.optima24.model.base;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Date;

import kg.optima.mobile.R;
import kg.optima.mobile.android.OptimaApp;
import kz.optimabank.optima24.utility.Constants;

public class ATFAccount implements Serializable {
    private static final long serialVersionUID = 4896856885629167518L;
    @SerializedName("balance")
    private double balance;
    @SerializedName("code")
    private int id;
    @SerializedName("is_visible")
    private boolean isVisible;
    @SerializedName("acc_type")
    private int acc_type;
    @SerializedName("Currency")
    private String currency;
    @SerializedName("custom_name")
    private String custom_name;
    @SerializedName("acc_num")
    private String acc_num;
    @SerializedName("multi_currency_info")
    private ArrayList<String> multi_currency_info;
    @SerializedName("is_blocked")
    private boolean is_blocked;
    @SerializedName("is_closed")
    private boolean is_closed;

    public ATFAccount() {
    }

    public ATFAccount(int id) {
        this.id = id;
    }

    public ATFAccount(String name) {
        this.id = Constants.HEADER_ID;
        this.custom_name = name;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public boolean getIs_blocked() {
        return is_blocked;
    }

    public boolean getIs_closed() {
        return is_closed;
    }


    public void setVisible(boolean bool) {
        this.isVisible = bool;
    }

    public String getNumber() {
        return acc_num;
    }

    public String getDisplayName() {
        if (TextUtils.isEmpty(custom_name)) {
            return acc_num;
        }
        return custom_name;
    }

    public String getCustom_name() {
        return custom_name;
    }

    public void setCustom_name(String name) {
        this.custom_name = name;
    }

//    public double getBalance() {
//        return balance;
//    }

//    public void setBalance(double balance) {
//        this.balance = balance;
//    }

    public int getId() {
        return id;
    }

    public ArrayList<String> getMulti_currency_info() {
        return multi_currency_info;
    }

    public String getCurrency() {
        return currency;
    }

    public double getBalance() {
        return balance;
    }

    public String getFormattedBalance() {
        BigDecimal bd = new BigDecimal(balance);
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');
        DecimalFormat formatter = new DecimalFormat("###,##0.00", symbols);
//        return String.format("%.2f %s", balance, Currency);
        return formatter.format(bd.doubleValue()) + " " + currency;
    }

    public String getFormattedBalanceInDetail() {
        BigDecimal bd = new BigDecimal(balance);
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');
        DecimalFormat formatter = new DecimalFormat("###,##0.00", symbols);
//        return String.format("%.2f %s", balance, Currency);
        String sum = "";

        sum = "<b>" + formatter.format(bd.doubleValue()) + " " + currency + "</b>";
        if (multi_currency_info != null) {
            if (multi_currency_info.size() > 0) {
                sum = OptimaApp.Companion.getInstance().getString(R.string.total_multi) + " &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  " + "<b>" + formatter.format(bd.doubleValue()) + " " + currency + "</b>";
                for (int i = multi_currency_info.size() - 1; i >= 0; i--) {
                    sum = sum + "<br/>" + multi_currency_info.get(i);
                }
            }
        }
        return sum;
    }

    //������ ������ � �������������� ������
    public ArrayList<String> getMulti_card_currency() {
        ArrayList<String> list = new ArrayList<String>();
        for(int i = 0; i<multi_currency_info.size(); i++){
            String s = multi_currency_info.get(i);
            String allCurrency = s.substring(s.lastIndexOf(' ') + 1);
            list.add(allCurrency);
        }
        return list;
    }

    public int getType() {
        return Constants.NO_ACCOUNT;
    }

    public int getAccType() {
        return acc_type;
    }

    public int getStatementId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("%s:%.2f %s", custom_name, balance, currency);
    }

    public static class CardAccount extends ATFAccount {
        private static final long serialVersionUID = 1647444584469131511L;
        @SerializedName("blocked_amount")
        private double blocked_amount;
        @SerializedName("credit_limit_amount")
        private double credit_limit_amount;
        @SerializedName("debt_amount")
        private double debt_amount;
        @SerializedName("exp_date")
        private Date exp_date;
        @SerializedName("holder")
        private String holder;
        @SerializedName("loan_limit")
        private double loan_limit;
        @SerializedName("vendor_type")
        private String vendor_type;
        @SerializedName("product_scheme")
        private String product_scheme;
        @SerializedName("card_account_code")
        private int card_account_code;



        public String getBlocked_amount() {

            BigDecimal bd = new BigDecimal(blocked_amount);
            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
            symbols.setGroupingSeparator(' ');
            DecimalFormat formatter = new DecimalFormat("###,##0.00", symbols);
//        return String.format("%.2f %s", balance, Currency);
            return formatter.format(bd.doubleValue()) + " ";
        }


        public Date getExp_date() {
            return exp_date;
        }

        public String getHolder() {
            return holder;
        }

        public String getProduct_scheme() {
            return product_scheme;
        }

        public String getCredit_limit() {

            BigDecimal bd = new BigDecimal(credit_limit_amount);
            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
            symbols.setGroupingSeparator(' ');
            DecimalFormat formatter = new DecimalFormat("###,##0.00", symbols);
//        return String.format("%.2f %s", balance, Currency);
            return formatter.format(bd.doubleValue()) + " ";
        }

        public String getDebt_amount() {
            BigDecimal bd = new BigDecimal(debt_amount);
            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
            symbols.setGroupingSeparator(' ');
            DecimalFormat formatter = new DecimalFormat("###,##0.00", symbols);
//        return String.format("%.2f %s", balance, Currency);
            return formatter.format(bd.doubleValue()) + " ";
        }


        public double getLoan_limit() {
            return loan_limit;
        }

        public String getVendor_type() {
            return vendor_type;
        }

        @Override
        public int getType() {
            return Constants.CARD_ACCOUNT;
        }

        @Override
        public int getStatementId() {
            return card_account_code;
        }

        @Override
        public String toString() {
            return "card " + super.toString();
        }
    }

    public static class LoanAccount extends ATFAccount {
        private static final long serialVersionUID = 7367936263743995287L;
        @SerializedName("AgreementNumber")
        private String AgreementNumber;
        @SerializedName("repay_acc_id")
        private int repay_acc_id;
        @SerializedName("AgreementDate")
        private Date AgreementDate;
        @SerializedName("NextPaymentDate")
        private Date NextPaymentDate;
        @SerializedName("AgreementEndDate")
        private Date AgreementEndDate;
        @SerializedName("repay_amount")
        private double repay_amount;
        @SerializedName("NextPaymentAmount")
        private double NextPaymentAmount;
        @SerializedName("InterestRate")
        private double InterestRate;
        @SerializedName("InterestAmount")
        private double InterestAmount;
        @SerializedName("AgreementAmount")
        private String AgreementAmount;

        public String getAgreementNumber() {
            return AgreementNumber;
        }

        public int getRepay_acc_id() {
            return repay_acc_id;
        }

        public Date getAgreementDate() {
            return AgreementDate;
        }

        public Date getNextPaymentDate() {
            return NextPaymentDate;
        }

        public Date getAgreementEndDate() {
            return AgreementEndDate;
        }

        public String getAgreementAmount() {

            BigDecimal bd = new BigDecimal(AgreementAmount);
            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
            symbols.setGroupingSeparator(' ');
            DecimalFormat formatter = new DecimalFormat("###,##0.00", symbols);
//        return String.format("%.2f %s", balance, Currency);
            return formatter.format(bd.doubleValue()) + " ";
//			return AgreementAmount;
        }

        public String getNextPaymentAmount() {
            BigDecimal bd = new BigDecimal(NextPaymentAmount);
            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
            symbols.setGroupingSeparator(' ');
            DecimalFormat formatter = new DecimalFormat("###,##0.00", symbols);
//        return String.format("%.2f %s", balance, Currency);
            return formatter.format(bd.doubleValue()) + " ";
        }

        public double getInterestRate() {
            return InterestRate;
        }

        public double getInterestAmount() {
            return InterestAmount;
        }

        @Override
        public int getType() {
            return Constants.LOAN_ACCOUNT;
        }

        @Override
        public String toString() {
            return "loan " + super.toString();
        }
    }

    public static class CurrentAccount extends ATFAccount {
        private static final long serialVersionUID = -7449636334546342472L;
        @SerializedName("minimum_balance")
        private double minimum_balance;

        public double getMinimum_balance() {
            return minimum_balance;
        }

        @Override
        public int getType() {
            return Constants.CURRENT_ACCOUNT;
        }

        @Override
        public String toString() {
            return "current " + super.toString();
        }
    }

    public static class DepositAccount extends ATFAccount {
        private static final long serialVersionUID = 7210547845372090341L;
        @SerializedName("ExpireDate")
        private Date ExpireDate;
        @SerializedName("OpenDate")
        private Date OpenDate;
        @SerializedName("Rate")
        private double Rate;
        @SerializedName("MinBalance")
        private double MinBalance;
        @SerializedName("AgreementNumber")
        private String AgreementNumber;
        @SerializedName("Capitalization")
        private boolean Capitalization;
        @SerializedName("IsWithDrawal")
        private boolean withDrawal;


        public boolean isWithDrawal() {
            return withDrawal;
        }

        public Date getExpireDate() {
            return ExpireDate;
        }

        public Date getOpenDate() {
            return OpenDate;
        }

        public String getAgreementNumber() {
            return AgreementNumber;
        }

        public boolean getCapitalization() {
            return Capitalization;
        }

        public double getRate() {
            return Rate;
        }

        public String getMinBalance() {
            BigDecimal bd = new BigDecimal(MinBalance);
            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
            symbols.setGroupingSeparator(' ');
            DecimalFormat formatter = new DecimalFormat("###,##0.00", symbols);
//        return String.format("%.2f %s", balance, Currency);
            return formatter.format(bd.doubleValue()) + " ";
        }

        @Override
        public int getType() {
            return Constants.DEPOSIT_ACCOUNT;
        }

        @Override
        public String toString() {
            return "deposit " + super.toString();
        }
    }
}
