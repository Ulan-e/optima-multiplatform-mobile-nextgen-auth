package kz.optimabank.optima24.db.entry;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
  Created by Timur on 14.01.2017.
 */

public class Sender extends RealmObject {
    public static final int SENDER_SOUND = 0;
    public static final int SENDER_MUTE = 1;
    public static final int SENDER_BLOCK = 2;

    public static final String SENDER_ALL_ID = "-1337";

    @PrimaryKey
    private String id;
    private String title;
    private boolean isBank;
    private int mode;

    @Ignore
    private String imageURL;

    public Sender() {
    }

    public Sender(String title, String id, boolean isBank) {
        this.title = title;
        this.id = id;
        this.isBank = isBank;
        this.mode = SENDER_SOUND;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isBank() {
        return isBank;
    }

    public void setBank(boolean isBank) {
        this.isBank = isBank;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public String getImageURL() {
//        if (imageURL == null) {
//            imageURL = String.format(RESTEngine.IMAGE_URL_FORMAT, getId());
//        }
//        return imageURL;
//    }
}
