package kz.optimabank.optima24.db.entry;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import kz.optimabank.optima24.utility.Constants;

public class Dictionary extends RealmObject implements Serializable {
    @PrimaryKey
    @SerializedName("Code")
    private String code;
    @SerializedName("Type")
    private int type;
    @SerializedName("Id")
    private int id;
    @SerializedName("Description")
    public String description;

    @Ignore
    private String custom_name;

    public Dictionary() {
    }

    public Dictionary(String name) {
        this.id = Constants.HEADER_ID;
        this.custom_name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public String getCustom_name() {
        return custom_name;
    }
}
