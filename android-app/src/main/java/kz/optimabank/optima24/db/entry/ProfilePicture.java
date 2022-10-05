package kz.optimabank.optima24.db.entry;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Rasul on 23.04.2018.
 */

public class ProfilePicture extends RealmObject {
    @PrimaryKey
    public String phone;
    public byte[] picture;

    public ProfilePicture(String phone, byte[] picture) {
        this.phone = phone;
        this.picture = picture;
    }

    public ProfilePicture() {}
}
