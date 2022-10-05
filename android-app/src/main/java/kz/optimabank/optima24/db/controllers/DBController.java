package kz.optimabank.optima24.db.controllers;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;

/**
  Created by Timur on 13.01.2017.
 */

public class DBController {
    Realm mRealm;

    public DBController() {
        RealmConfiguration mRealmConfig = LocalRealmConfig.get();
        try {
            mRealm = Realm.getInstance(mRealmConfig);
        } catch (RealmMigrationNeededException e) {
            Realm.deleteRealm(mRealmConfig);
            mRealm = Realm.getInstance(mRealmConfig);
        }
    }

    public Realm getRealm () {
        return mRealm;
    }

    public void close() {
        mRealm.close();
    }
}
