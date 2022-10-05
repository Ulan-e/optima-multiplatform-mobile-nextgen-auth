package kz.optimabank.optima24.db.entry;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Rasul on 09.02.2018.
 */

public class ForeignBank extends RealmObject implements Parcelable {
    @PrimaryKey
    @SerializedName("Id")
    public int id;
    @SerializedName("Bic")
    public String bic;
    @SerializedName("Name")
    public String name;
    @SerializedName("Country")
    public String country;
    @SerializedName("Address")
    public String address;
    @SerializedName("Description")
    public String description;

    public ForeignBank() {}

    public ForeignBank(int id, String bic, String name, String country, String address, String description) {
        this.id = id;
        this.bic = bic;
        this.name = name;
        this.country = country;
        this.address = address;
        this.description = description;
    }

    protected ForeignBank(Parcel in) {
        id = in.readInt();
        bic = in.readString();
        name = in.readString();
        country = in.readString();
        address = in.readString();
        description = in.readString();
    }

    public static final Creator<ForeignBank> CREATOR = new Creator<ForeignBank>() {
        @Override
        public ForeignBank createFromParcel(Parcel in) {
            return new ForeignBank(in);
        }

        @Override
        public ForeignBank[] newArray(int size) {
            return new ForeignBank[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(bic);
        dest.writeString(name);
        dest.writeString(country);
        dest.writeString(address);
        dest.writeString(description);
    }
}
