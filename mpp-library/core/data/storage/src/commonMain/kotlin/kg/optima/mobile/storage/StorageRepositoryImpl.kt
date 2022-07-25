package kg.optima.mobile.storage

import com.russhwolf.settings.Settings
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json


class StorageRepositoryImpl(
   private val settings: Settings,
   private val json: Json
) : StorageRepository {
   override fun remove(key: String) = settings.remove(key)

   override fun hasKey(key: String) = settings.hasKey(key)

   override fun <T> putObject(obj: T, serializer: KSerializer<T>, key: String) {
	  val json = json.encodeToString(serializer, obj)
	  putString(key, json)
   }

   override fun <T> getObject(serializer: KSerializer<T>, key: String, defaultValue: T): T {
      val objJson = getString(key)
      return when {
         objJson.isNullOrBlank() -> defaultValue
         else -> {
            try {
               json.decodeFromString(serializer, objJson)
            } catch (e: Exception) {
               defaultValue
            }
         }
      }
   }

   override fun <T> getObject(serializer: KSerializer<T>, key: String): T? {
      val objJson = getString(key)
      return when {
         objJson.isNullOrBlank() -> null
         else -> {
            try {
               json.decodeFromString(serializer, objJson)
            } catch (e: Exception) {
               null
            }
         }
      }
   }

   override fun putString(key: String, value: String) = settings.putString(key, value)
   override fun getString(key: String, defaultValue: String) = settings.getString(key, defaultValue)
   override fun getString(key: String): String? = settings.getStringOrNull(key)

   override fun putInt(key: String, value: Int) = settings.putInt(key, value)
   override fun getInt(key: String, defaultValue: Int) = settings.getInt(key, defaultValue)
   override fun getInt(key: String): Int? = settings.getIntOrNull(key)

   override fun putLong(key: String, value: Long) = settings.putLong(key, value)
   override fun getLong(key: String, defaultValue: Long) = settings.getLong(key, defaultValue)
   override fun getLong(key: String): Long? = settings.getLongOrNull(key)

   override fun putBoolean(key: String, value: Boolean) = settings.putBoolean(key, value)
   override fun getBoolean(key: String, defaultValue: Boolean) = settings.getBoolean(key, defaultValue)
   override fun getBoolean(key: String): Boolean? = settings.getBooleanOrNull(key)
}