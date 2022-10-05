package kz.optimabank.optima24.db.entry;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by Жексенов on 14.11.2014.
 * <p/>
 */
public class Region extends RealmObject {
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