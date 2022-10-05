package kz.optimabank.optima24.model.gson.response;

import android.annotation.SuppressLint;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import kz.optimabank.optima24.utility.Constants;

public class LoanScheduleResponse {
    public int id;
    public String name;

    @SerializedName("amount")
    private double amount;
    @SerializedName("commission")
    private double commission;
    @SerializedName("bank_interest")
    private double bank_interest;
    @SerializedName("currency")
    private String currency;
    @SerializedName("date")
    public String date;
    @SerializedName("main_debt")
    private double main_debt;
    @SerializedName("remain_debt")
    private double remain_debt;

    public LoanScheduleResponse(int id) {
        this.id = id;
    }

    public LoanScheduleResponse(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getDate() {
        String stDate = null;
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getDefault());
        try {
            if(date!=null) {
                stDate = Constants.VIEW_DATE_FORMAT.format(sdf.parse(date));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stDate;
    }

    public double getAmount() {
        return amount;
    }


    public double getCommission() {
        return commission;
    }

    public double getInterest() {
        return bank_interest;
    }

    public String getCurrency() {
        return currency;
    }

    public double getMainDebt() {
        return main_debt;
    }

    public double getDebt() {
        return remain_debt;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public void setInterest(double interest) {
        this.bank_interest = interest;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMainDebt(double mainDebt) {
        this.main_debt = mainDebt;
    }

    public void setDebt(double debt) {
        this.remain_debt = debt;
    }
}
