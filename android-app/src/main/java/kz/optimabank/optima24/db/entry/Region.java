package kz.optimabank.optima24.db.entry;

import com.google.gson.annotations.SerializedName;

import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Created by Жексенов on 14.11.2014.
 * <p/>
 */
public class Region {
    //        private static final long serialVersionUID = 7367936263743995287L;
    @SerializedName("Alias")
    private String alias;
    @SerializedName("ExternalId")
    private String externalId;
    @SerializedName("Id")
    private int id;
    @SerializedName("Name")
    private String name;

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


}