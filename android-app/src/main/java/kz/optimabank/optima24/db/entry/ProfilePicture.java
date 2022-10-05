package kz.optimabank.optima24.db.entry;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Created by Rasul on 23.04.2018.
 */

@Entity
public class ProfilePicture {
    @PrimaryKey
    public String phone;
    public byte[] picture;

    public ProfilePicture(String phone, byte[] picture) {
        this.phone = phone;
        this.picture = picture;
    }

    public ProfilePicture() {}
}
