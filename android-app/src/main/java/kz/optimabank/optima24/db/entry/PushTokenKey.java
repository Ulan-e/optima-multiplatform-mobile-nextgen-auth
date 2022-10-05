package kz.optimabank.optima24.db.entry;

import io.realm.RealmObject;

/**
  Created by Timur on 16.01.2017.
 */

public class PushTokenKey extends RealmObject {
    private String TokenKey;

    public String getTokenKey() {
        return TokenKey;
    }

    public void setTokenKey(String tokenKey) {
        TokenKey = tokenKey;
    }
}
