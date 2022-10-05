package kz.optimabank.optima24.db.controllers;

import io.realm.Realm;
import kz.optimabank.optima24.db.entry.DHKey;

/**
  Created by Timur on 07.08.2017.
 */

public class DHKeyController extends DBController {
    private static DHKeyController controller;

    public static DHKeyController getController() {
        if(controller == null) {
            return new DHKeyController();
        } else {
            return controller;
        }
    }

    public void setKey(final byte[] key) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(DHKey.class);
                DHKey realmKey = realm.createObject(DHKey.class);
                realmKey.setKey(key);
            }
        });
    }

    public byte[] getKey() {
        DHKey realmKey = mRealm.where(DHKey.class).findFirst();
        if(realmKey!=null) {
            return realmKey.getKey();
        }
        return null;
    }
}
