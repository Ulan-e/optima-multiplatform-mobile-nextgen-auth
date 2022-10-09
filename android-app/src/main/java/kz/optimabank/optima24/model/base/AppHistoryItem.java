package kz.optimabank.optima24.model.base;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class AppHistoryItem implements Serializable {
    @SerializedName("Name")
    public String Name;
    @SerializedName("Timestamp")
    public Date Timestamp;
    @SerializedName("LastUpdate")
    public Date LastUpdate;
    @SerializedName("Id")
    public int Id;
    @SerializedName("State")
    public int State;
    @SerializedName("ClientPhone")
    public String ClientPhone;
    @SerializedName("ClientName")
    public String ClientName;
    @SerializedName("Parameters")
    public ArrayList<AppHistoryParameter> Parameters;

    public void setState(int state) {
        State = state;
    }

    public class AppHistoryParameter implements Serializable {
        @SerializedName("Value")
        public String Value;
        @SerializedName("Label")
        public String Label;
        @SerializedName("ApplicationId")
        public int ApplicationId;
        @SerializedName("ApplicationTypeParameterId")
        public int ApplicationTypeParameterId;
        @SerializedName("Id")
        public int Id;
        @SerializedName("Type")
        public int Type;
    }
}
