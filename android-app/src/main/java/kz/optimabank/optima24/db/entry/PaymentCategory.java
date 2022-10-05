package kz.optimabank.optima24.db.entry;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Date;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import kz.optimabank.optima24.utility.Constants;

@Entity
public class PaymentCategory implements Serializable{
    public int code = Constants.ITEM_ID;
    public boolean isTemplate = false;
    public String headerName;
    
    @PrimaryKey
    @SerializedName("Alias")
    public String alias;
    @SerializedName("ExternalId")
    private String externalId;
    @SerializedName("Id")
    private int id;
    @SerializedName("Name")
    private String name;

    public PaymentCategory() {
    }

    public PaymentCategory(int code, String name) {
        this.name = name;
        this.code = code;
    }

    public String getAlias() {
        return alias;
    }

    public String getExternalId() {
        return externalId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static class PaymentService {
        @SerializedName("Alias")
        private String alias;
        @SerializedName("Id")
        private int Id;
        @SerializedName("ExternalId")
        private int ExternalId;
        @SerializedName("AllowedForGetBalance")
        private boolean isAllowedGetBalance;
        @SerializedName("Name")
        private String Name;
        @SerializedName("Description")
        private String description;
        @SerializedName("PaymentCategoryId")
        private int paymentCategoryId;
        @SerializedName("Parameters")
        private ArrayList<Parameter> parameters;
        @SerializedName("Fee")
        private double Fee;
        @SerializedName("IsInvoiceable")
        private boolean IsInvoiceable;
        @SerializedName("IsPenalties")
        private boolean IsPenalties;
        @SerializedName("Regions")
        private ArrayList<Region> regions;


        public ArrayList<Region> getRegions() {
            return regions;
        }

        public boolean isAllowedGetBalance() {

            return isAllowedGetBalance;
        }


        public boolean IsInvoiceable() {
            return IsInvoiceable;
        }

        public boolean IsPenalties() {
            return IsPenalties;
        }

        public String getAlias() {
            return alias;
        }

        public int getId() {
            return Id;
        }

        public double getFee() {
            return Fee;
        }

        public String getName() {
            return Name;
        }

        public int getExternalId() {
            return ExternalId;
        }

        public String getDescription() {
            return description;
        }

        public int getPaymentCategoryId() {
            return paymentCategoryId;
        }

        public ArrayList<Parameter> getParameters() {
            return parameters;
        }

        @Override
        public String toString() {
            return Name;
        }

        public class Parameter {
            @SerializedName("Alias")
            private String alias;
            @SerializedName("Description")
            private String description;
            @SerializedName("ExternalId")
            private String externalId;
            @SerializedName("Id")
            private int id;
            @SerializedName("Name")
            private String name;
            @SerializedName("Mask")
            private String Mask;
            @SerializedName("Value")
            private String value;
            @SerializedName("UserSample")
            private String UserSample;


            public String getAlias() {
                return name;
            }

            public String getDescription() {
                return description;
            }

            public String getExternalId() {
                return externalId;
            }

            public String getMask() {
                return Mask;
            }

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public String getUserSample() {
                return UserSample;
            }

            public String getValue() {
                return value;
            }

            @Override
            public String toString() {
                return name;
            }
        }
    }

    public static class Template extends Object implements Serializable {
        @SerializedName("Id")
        private int id;
        @SerializedName("Name")
        private String Name;
        @SerializedName("ServiceId")
        private int ServiceId;
        @SerializedName("SourceAccountId")
        private int SourceAccountId;
        @SerializedName("Amount")
        private double Amount;
        @SerializedName("Parameters")
        private ArrayList<TemplateParameter> parameters;

        @SerializedName("StandingInstructionTime")
        private String AutoPayTime;

        @SerializedName("PayAmountType")
        private int PayAmountType;

        @SerializedName("StandingInstructionDate")
        private String AutoPayDate;

        @SerializedName("StandingInstructionType")
        private int AutoPayType;

        @SerializedName("AutoTransferCount")
        private int AutoPayCount;

        @SerializedName("StandingInstructionStatus")
        private int AutoPayStatus;

        @SerializedName("IsAutoPay")
        private boolean autoPay;

        @SerializedName("AutoTransferDateBegin")
        private Date AutoPayDateBegin;

        public boolean isAutoPay() {
            return autoPay;
        }

        public int getPayAmountType() {
            return PayAmountType;
        }

        public Date getAutoPayDateBegin() {
            return AutoPayDateBegin;
        }

        public int getAutoPayCount() {
            return AutoPayCount;
        }

        public String getAutoPayDate() {
            return AutoPayDate;
        }

        public int getAutoPayStatus() {
            return AutoPayStatus;
        }

        public int getAutoPayType() {
            return AutoPayType;
        }

        public String getAutoPayTime() {
            return AutoPayTime;
        }

        public int getId() {
            return id;
        }

        public int getSourceAccountId() {
            return SourceAccountId;
        }

        public String getAmount() {
            BigDecimal bd = new BigDecimal(Amount);
            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
            symbols.setGroupingSeparator(' ');
            DecimalFormat formatter = new DecimalFormat("###,##0.00", symbols);
//        return String.format("%.2f %s", balance, Currency);
            return formatter.format(bd.doubleValue()) + " ";
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            this.Name = name;
        }

        public int getServiceId() {
            return ServiceId;
        }

        public ArrayList<TemplateParameter> getParameters() {
            return parameters;
        }

        @Override
        public String toString() {
            return Name;
        }

        public static class TemplateParameter implements Serializable {
            @SerializedName("Id")
            private int id;
            @SerializedName("ServiceParameterId")
            private int serviceParameterId;
            @SerializedName("SubscriptionId")
            private int subscriptionId;
            @SerializedName("Value")
            private String value;

            public int getId() {
                return id;
            }

            public int getServiceParameterId() {
                return serviceParameterId;
            }

            public int getSubscriptionId() {
                return subscriptionId;
            }

            public String getValue() {
                if (value == null) {
                    value = "";
                }
                return value.replaceAll("\\s", "");
            }

            @Override
            public String toString() {
                return value;
            }
        }
    }
}
