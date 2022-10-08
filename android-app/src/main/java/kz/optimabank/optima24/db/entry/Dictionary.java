package kz.optimabank.optima24.db.entry;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import kz.optimabank.optima24.utility.Constants;

@Entity
public class Dictionary implements Serializable {

    @SerializedName("Code")
    private String code;
    @SerializedName("Type")
    private int type;
    @SerializedName("Id")
    @PrimaryKey
    @NonNull
    private int id;
    @SerializedName("Description")
    public String description;

    @Ignore
    private String custom_name;

    public void setCode(String code) {
        this.code = code;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCustom_name(String custom_name) {
        this.custom_name = custom_name;
    }

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
