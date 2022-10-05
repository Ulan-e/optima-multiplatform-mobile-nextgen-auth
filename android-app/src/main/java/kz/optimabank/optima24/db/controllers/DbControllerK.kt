package kz.optimabank.optima24.db.controllers

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.exceptions.RealmException

open class DBController {
    var mRealm: Realm? = null
    val realm: Realm?
        get() = mRealm

    fun close() {
        mRealm?.close()
    }

    init {
        val mRealmConfig: RealmConfiguration? = LocalRealmConfigK.get()
        mRealm = try {
            Realm(mRealmConfig)
        } catch (e: RealmException) {
            Realm.deleteRealm(mRealmConfig)
            Realm.getInstance(mRealmConfig)
        }
    }
}