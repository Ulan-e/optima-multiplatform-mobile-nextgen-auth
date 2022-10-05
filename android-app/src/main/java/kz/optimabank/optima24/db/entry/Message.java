package kz.optimabank.optima24.db.entry;

import android.annotation.SuppressLint;

import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import kz.optimabank.optima24.model.base.PushResponse;
import kz.optimabank.optima24.utility.Constants;

/**
  Created by Timur on 14.01.2017.
 */

public class Message {
    public static final int MESSAGE_FINANCE = 0;
    public static final int MESSAGE_AUTHORIZE = 1;
    public static final int MESSAGE_INFO = 2;
    public static final int MESSAGE_INFO_HTML = 3;

    @Ignore
    public String name;
    @Ignore
    public int code = Constants.ITEM_ID;

    @PrimaryKey
    private String id;
    private int type;
    //    private Sender sender;
    private String content;
    private String date;
    private String currency;
    private double amount;
    @SerializedName("Checked")
    private boolean checked=false;
    private boolean isReader=false;

    @Ignore
    private String senderTitle;
    @Ignore
    private String senderId;

    public Message() {
    }

    public Message(int code, String name) {
        this.name = name;
        this.code = code;
    }

    public Message(PushResponse.MessagePayload payload, boolean fullMessage) {
        this.date = payload.timestamp_utc;
        this.content = payload.text;
        this.id = payload.id;
        this.type = payload.type;
        this.senderTitle = payload.src;
        this.senderId = payload.src_id;

        if (!fullMessage) return;

        if (type == MESSAGE_FINANCE) {
            this.amount = payload.finance.amount;

            String currency = payload.finance.currency;
//            if (Currency != null &&
//                    DataManager.getInstance().getCurrencyDictionary().containsKey(Currency)) {
//                this.Currency = Currency;
//            } else {
            this.currency = Constants.CURRENCY_KZT;

            if (payload.finance.type == PushResponse.MessagePayload.Finance.FINANCE_DEBIT) {
                this.amount = -this.amount;
            }
        } else {
            this.amount = 0.0;
            this.currency = Constants.CURRENCY_KZT;
        }
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getSenderTitle() {
        return senderTitle;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void toggleChecked() {
        checked = !checked;
    }

    public boolean isReader() {
        return isReader;
    }

    public void setReader(boolean reader) {
        isReader = reader;
    }

    public String getOperationDate(SimpleDateFormat simpleDateFormat) {
        String stDate = null;
        SimpleDateFormat sdf = getSimpleDateFormat();
        try {
            stDate = simpleDateFormat.format(sdf.parse(date));
            TimeUnit.MILLISECONDS.toDays(sdf.parse(date).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stDate;
    }

    private SimpleDateFormat getSimpleDateFormat() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf;
    }

    //    public Sender getSender() {
//        return sender;
//    }
//
//    public void setSender(Sender sender) {
//        this.sender = sender;
//    }
}
