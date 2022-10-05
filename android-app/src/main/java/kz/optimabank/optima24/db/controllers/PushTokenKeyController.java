package kz.optimabank.optima24.db.controllers;

import io.realm.Realm;
import kz.optimabank.optima24.db.entry.PushTokenKey;

/**
  Created by Timur on 07.08.2017.
 */

public class PushTokenKeyController extends DBController {
    private static PushTokenKeyController controller;

    public static PushTokenKeyController getController() {
        if(controller == null) {
            return new PushTokenKeyController();
        } else {
            return controller;
        }
    }

    public void setPushTokenKey(final String pushTokenKey) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(PushTokenKey.class);
                PushTokenKey realmKey = realm.createObject(PushTokenKey.class);
                realmKey.setTokenKey(pushTokenKey);
            }
        });
    }

    public String getPushTokenKey() {
        PushTokenKey realmKey = mRealm.where(PushTokenKey.class).findFirst();
        if(realmKey!=null) {
            return realmKey.getTokenKey();
        }
        return null;
    }

}
