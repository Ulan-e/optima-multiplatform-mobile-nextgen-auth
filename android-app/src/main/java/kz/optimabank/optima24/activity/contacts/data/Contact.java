package kz.optimabank.optima24.activity.contacts.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.IntegerRes;

public class Contact implements Parcelable {
    private String contactName;
    private String phoneNumber;
    private String formattedPhoneNumber;
    private int imageUri;

    public Contact(String contactName, String phoneNumber) {
        this.contactName = contactName;
        this.phoneNumber = phoneNumber;
    }

    protected Contact(Parcel in) {
        this.contactName = in.readString();
        this.phoneNumber = in.readString();
        this.formattedPhoneNumber = in.readString();
        this.imageUri = in.readInt();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getImageUri() {
        return imageUri;
    }

    public void setImageUri(int imageUri) {
        this.imageUri = imageUri;
    }

    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }

    public void setFormattedPhoneNumber(String formattedPhoneNumber) {
        this.formattedPhoneNumber = formattedPhoneNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(contactName);
        dest.writeString(phoneNumber);
        dest.writeString(formattedPhoneNumber);
        dest.writeInt(imageUri);
    }
}