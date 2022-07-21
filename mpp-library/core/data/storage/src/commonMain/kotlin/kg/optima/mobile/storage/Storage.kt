package kg.optima.mobile.storage

import kotlinx.serialization.KSerializer

interface Storage {
   fun remove(key: String)

   fun hasKey(key: String): Boolean

   fun <T> putObject(obj: T, serializer: KSerializer<T>, key: String)

   fun <T> getObject(serializer: KSerializer<T>, key: String, defaultValue: T): T

   fun <T> getObject(serializer: KSerializer<T>, key: String): T?

   fun putString(key: String, value: String)
   fun getString(key: String, defaultValue: String): String
   fun getString(key: String): String?

   fun putInt(key: String, value: Int)
   fun getInt(key: String, defaultValue: Int): Int
   fun getInt(key: String): Int?

   fun putLong(key: String, value: Long)
   fun getLong(key: String, defaultValue: Long): Long
   fun getLong(key: String): Long?

   fun putBoolean(key: String, value: Boolean)
   fun getBoolean(key: String, defaultValue: Boolean): Boolean
   fun getBoolean(key: String): Boolean?
}