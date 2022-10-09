package kz.optimabank.optima24.model.base;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class HistoryDetailsApplications implements Serializable {

    @SerializedName("Id")
    public int id;

    @SerializedName("Name")
    public String Name;

    @SerializedName("Number")
    public String Number;

    @SerializedName("Date")
    public String Date;

    @SerializedName("Status")
    public String Status;

    @SerializedName("StatusClass")
    public String StatusClass;

    @SerializedName("Url")
    public String Url;

    @SerializedName("Cancel")
    public boolean Cancel;

    @SerializedName("Types")
    public ArrayList<Types> Types;

    @SerializedName("Records")
    public ArrayList<Records> Records;

    @SerializedName("IsSavingApplication")
    public boolean IsSavingApplication;

    public static class Types implements Serializable {

        @SerializedName("NeedValidate")
        public boolean NeedValidate;

        @SerializedName("Id")
        public int id;

        @SerializedName("Name")
        public String Name;

        @SerializedName("Type")
        public int Type;

        @SerializedName("Description")
        public String Description;

        @SerializedName("NavigationLink")
        public String NavigationLink;

        @SerializedName("Value")
        public String Value;

        @SerializedName("SourceAccountCode")
        public String SourceAccountCode;

        public Types(String name, String value){
            this.Name = name;
            this.Value = value;
        }
    }

    public class Records implements Serializable{

        @SerializedName("Date")
        public String Date;

        @SerializedName("Status")
        public String Status;

        @SerializedName("Comment")
        public String Comment;

    }

}