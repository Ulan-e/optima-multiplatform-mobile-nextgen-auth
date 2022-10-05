package kz.optimabank.optima24.db.controllers;

import io.realm.RealmConfiguration;
import kz.optimabank.optima24.db.module.LocalModule;

class LocalRealmConfig {

    private static RealmConfiguration localRealmConfig;

    static RealmConfiguration get() {
        if(localRealmConfig != null) {
            return localRealmConfig;
        }

        localRealmConfig = new RealmConfiguration
                .Builder()
                .modules(new LocalModule())
                .build();
        return localRealmConfig;
    }
}
