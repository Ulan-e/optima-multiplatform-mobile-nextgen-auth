package kz.optimabank.optima24.model.base;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ApplicationTypeDto implements Serializable {
    @SerializedName("Name")
    public String Name;

    @SerializedName("Id")
    public int id;

    @SerializedName("CategoryId")
    public int CategoryId;

    @SerializedName("Type")
    public int Type;

    @SerializedName("NeedValidate")
    public boolean NeedValidate;

    @SerializedName("IsEnabled")
    public boolean IsEnabled;

    @SerializedName("IsPublic")
    public boolean IsPublic;

    @SerializedName("Description")
    public String Description;

    @SerializedName("CategoryName")
    public String CategoryName;

    @SerializedName("NavigationLink")
    public String NavigationLink;

    @SerializedName("SourceAccountCode")
    public String SourceAccountCode;

    @SerializedName("Value")
    public String Value;

    @SerializedName("Timestamp")
    public String Timestamp;

    @SerializedName("Parameters")
    public ArrayList<ApplicationTypeParameter> Parameters;

    @SerializedName("Params")
    public ArrayList<ApplicationParamModel> Params;

    public ApplicationTypeDto(int id, String name, String value){
        this.id = id;
        this.Value = value;
        this.Name = name;
    }

//    @SerializedName("AccountList")
//    public ArrayList<UserAccounts> AccountList;

    public class ApplicationTypeParameter {

        @SerializedName("Id")
        public long id;

        @SerializedName("ApplicationTypeId")
        public long  ApplicationTypeId;

        @SerializedName("Name")
        public String Name;

        @SerializedName("Type")
        public int Type;

        @SerializedName("ParamOrder")
        public short ParamOrder;

        @SerializedName("RegExp")
        public String RegExp;

        @SerializedName("Required")
        public boolean Required;

        @SerializedName("DefaultValue")
        public String DefaultValue;

        @SerializedName("ParamLabel")
        public String ParamLabel;

        @SerializedName("ParamComments")
        public String ParamComments;

        @SerializedName("ParamValue")
        public String ParamValue;

    }



    //need for create/take request

    public class ApplicationParamModel implements Serializable {

        @SerializedName("Name")
        public String Name;

        @SerializedName("Value")
        public String Value;

        @SerializedName("Id")
        public long Id;

        @SerializedName("Type")
        public int Type;

        @SerializedName("Placeholder")
        public String Placeholder;

        @SerializedName("Description")
        public String Description;

        @SerializedName("NavigationLink")
        public String NavigationLink;

        @SerializedName("Checked")
        public boolean Checked;

        @SerializedName("DropDownList")
        public ArrayList<DropdownData> DropDownList;

        @SerializedName("Currency")
        public String Currency;

        @SerializedName("Comment")
        public String Comment;

        @SerializedName("Required")
        public boolean Required;

    }

    public static class DropdownData implements Serializable {
        @SerializedName("Value")
        public String Value;

        @SerializedName("Id")
        public String Id;

        @SerializedName("JsonList")
        public String JsonList;

        @SerializedName("Checked")
        public boolean Checked;

        @Override
        public String toString() {
            return Value;
        }

        public DropdownData(String value) {
            Value = value;
        }

        public DropdownData(String Value, String Id, boolean Checked){
            this.Value = Value;
            this.Id = Id;
            this.Checked = Checked;
        }
    }

}