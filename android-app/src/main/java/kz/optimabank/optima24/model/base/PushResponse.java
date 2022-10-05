package kz.optimabank.optima24.model.base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
  Created by Timur on 13.01.2017.
 */

public class PushResponse implements Serializable {
    @SerializedName("r")
    public int result;
    /**
     * Response model for device registration
     */
    public class DeviceRegisterResponse extends PushResponse {
        @SerializedName("device_token")
        @Expose
        public String deviceId;
    }

    public class PushSettings extends PushResponse {
        @SerializedName("PushType")
        public int pushType;
        @SerializedName("Enabled")
        public boolean enabled;
    }

    public static class DeviceConfirmResponse extends PushResponse {
        @SerializedName("exchange")
        public String exchange;
        @SerializedName("tk")
        public String tk;

    }

    public static class MessageUnreadResponse extends PushResponse {
        @SerializedName("l")
        public ArrayList<String> unreadMessId;

    }
    public static class DeviceTokenUpdateResponse extends PushResponse {
    }

    /**
     * Response model for message details request
     */
    public static class MessageResponse extends PushResponse {
        @SerializedName("e")
        public String encrypted;
    }

    /**
     * Message payload model
     */
    public static class MessagePayload {
        @SerializedName("i")
        public String id;
        @SerializedName("s")
        public String src;
        @SerializedName("o")
        public String src_id;
        @SerializedName("z")
        public String timestamp_utc;
        @SerializedName("b")
        public String text;
        @SerializedName("t")
        public int type;
        @SerializedName("f")
        public Finance finance;
        @SerializedName("a")
        public Auth auth;

        /**
         * Financial message payload model
         */
        public static class Finance {
            public static final int FINANCE_DEBIT = 0;
            public static final int FINANCE_CREDIT = 1;

            @SerializedName("a")
            public double amount;
            @SerializedName("c")
            public String currency;
            @SerializedName("oa")
            public double originalAmount;
            @SerializedName("oc")
            public String originalCurrency;
            @SerializedName("z")
            public Date date;
            @SerializedName("t")
            public int type;
            @SerializedName("n")
            public String account;
            @SerializedName("d")
            public String description;
            @SerializedName("l")
            public String location;
        }

        /**
         * Authentication message payload model
         */
        public static class Auth {
            @SerializedName("f")
            public ArrayList<Field> fields;

            /**
             * Authentication message name/value model
             */
            public class Field {
                @SerializedName("n")
                public String name;
                @SerializedName("v")
                public String value;
            }
        }
    }
}
