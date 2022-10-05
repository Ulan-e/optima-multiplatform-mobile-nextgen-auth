package kz.optimabank.optima24.model.base;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.util.ArrayList;

import kz.optimabank.optima24.db.entry.Country;
import kz.optimabank.optima24.db.entry.Dictionary;
import kz.optimabank.optima24.db.entry.PaymentCategory;
import kz.optimabank.optima24.db.entry.PredefinedValues;
import kz.optimabank.optima24.db.entry.Region;
import kz.optimabank.optima24.model.base.ATFAccount.CardAccount;
import kz.optimabank.optima24.model.base.ATFAccount.CurrentAccount;
import kz.optimabank.optima24.model.base.ATFAccount.DepositAccount;
import kz.optimabank.optima24.model.base.ATFAccount.LoanAccount;
import kz.optimabank.optima24.model.base.HistoryItem.TransferHistoryItem;
import kz.optimabank.optima24.db.entry.PaymentCategory.PaymentService;
import kz.optimabank.optima24.db.entry.PaymentCategory.Template;
import kz.optimabank.optima24.model.gson.response.LoanScheduleResponse;

public class GSONModel {
    @SerializedName("success")
    public boolean success;
    @SerializedName("data")
    public JsonElement data;

    public static class GSONResponse {
        @SerializedName("response")
        public String response;
        @SerializedName("client_info")
        public String client_info;
        @SerializedName("is_template_exist")
        public boolean is_template_exist;

        public String getResponse() {
            return response;
        }
    }

    public static class GSONVersionApi {
        @SerializedName("response")
        public String response;
        @SerializedName("publications_must_be_updated")
        public boolean publications;
    }

    public static class GSONSession {
        @SerializedName("authorised")
        public boolean authorized;
        @SerializedName("error_description")
        public String error_description;
        @SerializedName("response")
        public String response;
        @SerializedName("session_id")
        public String session_id;
        @SerializedName("auth_status")
        public int auth_status;
        @SerializedName("user")
        public User user;
        @SerializedName("user_name")
        private String user_name;

        public String getUser_name() {
            return user_name;
        }
    }

    public static class User {
        @SerializedName("first_name")
        public String first_name;
        @SerializedName("idn")
        public String idn;
        @SerializedName("last_name")
        public String last_name;
        @SerializedName("middle_name")
        public String middle_name;
        @SerializedName("sex")
        public String sex;
        public String phone;

        public void setPhone(String phone) {
            this.phone = phone;
        }
        //   @SerializedName("sex")
//        public String sex;
    }

    public static class GSONNews {
        @SerializedName("news")
        public ArrayList<NewsItem> news;

    }

    public static class GSON_CHECK_SERVICE_POINTS {
        @SerializedName("atms_must_be_updated")
        public boolean atms_must_be_updated;
        @SerializedName("branches_must_be_updated")
        public boolean branches_must_be_updated;

    }


    public class GSONATMs {
//        @SerializedName("ServicePoints")
        public ArrayList<Terminal> servicePoints;
    }

    public static class GSONBranches {
        @SerializedName("ServicePoints")
        public ArrayList<Terminal> ServicePoints;
    }

    public static class GSON_APP_MAP {
        @SerializedName("apps_map")
        public JSONObject apps_map;
    }

    public static class GSONAccounts implements SerializedName {
        @SerializedName("accounts")
        public ArrayList<CurrentAccount> accounts;
        @SerializedName("cards")
        public ArrayList<CardAccount> cards;
        @SerializedName("loans")
        public ArrayList<LoanAccount> loans;
        @SerializedName("deposits")
        public ArrayList<DepositAccount> deposits;

        @Override
        public String value() {
            return null;
        }

        @Override
        public String[] alternate() {
            return new String[0];
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return null;
        }
    }

    public static class GSONSchedule {
        @SerializedName("payments")
        public ArrayList<LoanScheduleResponse> payments;
    }

    public static class GSONHistory {
        @SerializedName("payment_info")
        public ArrayList<HistoryItem> payment_info;
    }

    public static class GSONTransferHistory {
        @SerializedName("transfers")
        public ArrayList<TransferHistoryItem> transfers;
    }

    public static class GSONPayments {
        @SerializedName("__type")
        public String type;
        @SerializedName("payment_category")
        public ArrayList<PaymentCategory> payment_category;
        @SerializedName("payment_service")
        public ArrayList<PaymentService> payment_service;
        @SerializedName("regions")
        public ArrayList<Region> regions;
        @SerializedName("predefined_values")
        public ArrayList<PredefinedValues> predefined_values;

    }

    public static class GSONTransferTemplates {
        @SerializedName("transfer_templates")
        public ArrayList<TemplateTransfer> transfer_templates;
    }

//    public static class GSONRate {
//        @SerializedName("rates")
//        public ArrayList<RateFragment.Rate> rates;
//    }

    public static class GSONRateM2M {
        @SerializedName("rates")
        public ArrayList<Rate> rates;
    }

    public static class GSONSuccess extends GSONModel {
        @SerializedName("response")
        public String response;
        @SerializedName("description")
        public String description;
        @SerializedName("error_description")
        public String error_description;
        @SerializedName("fee")
        public String fee;
        @SerializedName("fee_currency")
        public String fee_currency;
        @SerializedName("calculation_info")
        public String calculation_info;
        @SerializedName("reference")
        public String reference;
        @SerializedName("knp")
        public ArrayList<Dictionary> knp;
        @SerializedName("seco")
        public ArrayList<Dictionary> seco;
        @SerializedName("bic")
        public ArrayList<Dictionary> bic;
        @SerializedName("countries")
        public ArrayList<Country> countries;
        @SerializedName("vo_codes")
        public ArrayList<Dictionary> vo_codes;

        public String getFee() {
            try {
//                for(char character  : fee.toCharArray()){
//                    Log.d("TAG", "character = " + character);
//                }
                double intFee = Double.parseDouble(fee.replaceAll(",","."));
                Log.d("TAG","intFee = " +intFee);
                return fee;
            } catch (NumberFormatException e) {
                Log.d("TAG", "fee length = " + fee.length());
                if(fee.length()==8) {
                    return fee.substring(0, 1) + fee.substring(2);
                } else if (fee.length()==9) {
                    return fee.substring(0, 2) + fee.substring(3);
                } else if(fee.length()==10) {
                    return fee.substring(0, 3) + fee.substring(4);
                } else {
                    return fee.substring(0, 4) + fee.substring(5);
                }
            }
        }
    }

    public static class GSONPaymentSuccess extends GSONSuccess {
        @SerializedName("template")
        public Template template;
    }

    public static class GSONError {
        @SerializedName("error_code")
        public int error_code;
        @SerializedName("error_description")
        public String error_description;
        @SerializedName("session_timed_out")
        public boolean session_timed_out;
    }

    public static class GSONCheckVersion {
        @SerializedName("success")
        public boolean success;
        @SerializedName("message")
        public String message;
        @SerializedName("code")
        public int code;
    }

    public static class GSONPaymentCheck extends GSONModel{
        @SerializedName("penalties")
        public ArrayList<PenaltyItem> penalties;
        @SerializedName("check_response_parameter")
        public GSONCheckParameter payment_parameter;
        @SerializedName("response")
        public String response;
        @SerializedName("reference")
        public String reference;
        @SerializedName("amount")
        public String debtSum;
        @SerializedName("fixComm")
        public String fixComm;
        @SerializedName("prcntComm")
        public String prcntComm;
        @SerializedName("is_template_exist")
        public boolean is_template_exist;
        @SerializedName("minComm")
        public String minComm;
        @SerializedName("fee")
        public String fee;

        public static class GSONCheckParameter {
            @SerializedName("Amount")
            public String amount;
            @SerializedName("Fee")
            public String fee;
            @SerializedName("ConfirmFee")
            public String totalFee;
            @SerializedName("TransactionCost")
            public String cost;
        }
    }

    public class GSON_type {
        @SerializedName("__type")
        public String type;
    }

    public class GsonRespPenalty {
        @SerializedName("fee")
        public String fee;
        @SerializedName("fixComm")
        public String fixComm;
        @SerializedName("minComm")
        public String minComm;
        @SerializedName("prcntComm")
        public String prcntComm;
    }
}
