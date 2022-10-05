package kz.optimabank.optima24.model.base;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StatementsWithStats implements Cloneable {

    @SerializedName("Operations")
    public ArrayList<ATFStatement> statements;

    @SerializedName("TotalIncome")
    public ArrayList<Data> totalIncome;

    @SerializedName("TotalExpense")
    public ArrayList<Data> totalExpense;

    @SerializedName("TotalFee")
    public Data totalFee;

    @SerializedName("EndBalance")
    public float endBalance;

    @SerializedName("StartBalance")
    public float startBalance;

    public class Data {
        @SerializedName("Currency")
        public String currency;

        @SerializedName("Value")
        public float value;
    }
}
