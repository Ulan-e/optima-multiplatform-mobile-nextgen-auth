package kz.optimabank.optima24.db.controllers

import io.realm.kotlin.RealmConfiguration
import kz.optimabank.optima24.db.module.LocalModule


object LocalRealmConfigK {

    private var localRealmConfig: RealmConfiguration? = null

    fun get(): RealmConfiguration? {
        if (localRealmConfig != null) {
            return localRealmConfig
        }
        localRealmConfig = RealmConfiguration.Builder()
            .modules(LocalModule())
            .build()
        return localRealmConfig
    }
}