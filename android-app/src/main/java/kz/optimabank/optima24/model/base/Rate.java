package kz.optimabank.optima24.model.base;

import android.annotation.SuppressLint;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import kz.optimabank.optima24.utility.Constants;

/**
  Created by Timur on 08.09.2016.
 */
public class Rate {

    public Rate(int code) {
        this.code = code;
    }

    public int code = 0;
    @SerializedName("BaseCurrency")
    public String BaseCurrency;
    @SerializedName("BuyRate")
    public String BuyRate;
    @SerializedName("ExchangeRate")
    public String ExchangeRate;
    @SerializedName("ForeignCurrency")
    public String ForeignCurrency;
    @SerializedName("RateNominal")
    public String RateNominal;
    @SerializedName("SellRate")
    public String SellRate;
    @SerializedName("UpdateTimeStamp")
    public String updateTimeStamp;
    @SerializedName("Type")
    public int type;

    public int getOrder() {
        if (getForeignCurrency() != null) {
            switch (getForeignCurrency()) {
                case "USD":
                    return 1;
                case "EUR":
                    return 2;
                case "RUB":
                    return 3;
                case "CNY":
                    return 4;
                default:
                    return 5;
            }
        } else return 0;
    }

    public String getBaseCurrency() {
        return BaseCurrency;
    }

    public String getBuyRate() {
        return BuyRate;
    }

    public String getExchangeRate() {
        return ExchangeRate;
    }

    public String getForeignCurrency() {
        return ForeignCurrency;
    }

    public String getRateNominal() {
        return RateNominal;
    }

    public String getSellRate() {
        return SellRate;
    }

    public String getDate() {
        String stDate = null;
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getDefault());
        try {
            stDate = Constants.VIEW_DATE_FORMAT.format(sdf.parse(updateTimeStamp));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return stDate;
    }
}
