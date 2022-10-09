package kg.optima.mobile.feature.auth.component

import kg.optima.mobile.base.platform.PlatformDate
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.storage.StorageRepository
import kg.optima.mobile.storage.cache.RuntimeCache

class AuthPreferencesImpl(
    private val storageRepository: StorageRepository,
//    private val runtimeCache: RuntimeCache,
) : AuthPreferences {

    override var clientId: String?
        get() = storageRepository.getString(AuthPreferences.CLIENT_ID)
        set(value) = storageRepository.putString(AuthPreferences.CLIENT_ID, value.orEmpty())

    override var isAuthorized: Boolean
        get() = storageRepository.getBoolean(AuthPreferences.AUTHORIZED, false)
        set(value) = storageRepository.putBoolean(AuthPreferences.AUTHORIZED, value)

    override var deviceId: String
        get() = storageRepository.getString(
            key = AuthPreferences.DEVICE_ID,
            defaultValue = PlatformDate.getTimeMills().also { id -> deviceId = id }
        )
        set(value) = storageRepository.putString(AuthPreferences.DEVICE_ID, value)

    override var sessionData: SessionData?
        get() = storageRepository.getObject(SessionData.serializer(), AuthPreferences.SESSION_DATA)
        set(value) {
            if (value != null) {
                storageRepository.putObject(value, SessionData.serializer(), AuthPreferences.SESSION_DATA)
            } else {
                storageRepository.remove(AuthPreferences.SESSION_DATA)
            }
        }
    override var userInfo: UserInfo?
        get() = storageRepository.getObject(UserInfo.serializer(), AuthPreferences.USER_INFO)
        set(value) {
            if (value != null) {
                storageRepository.putObject(value, UserInfo.serializer(), AuthPreferences.USER_INFO)
            } else {
                storageRepository.remove(AuthPreferences.USER_INFO)
            }
        }

    override var password: String
        get() = storageRepository.getString("PASSWORD", emptyString)
        set(value) = storageRepository.putString("PASSWORD", value)

    override var pin: String
        get() = storageRepository.getString("PIN", emptyString)
        set(value) = storageRepository.putString("PIN", value)

    override var biometry: String
        get() = storageRepository.getString("Biometry", emptyString)
        set(value) = storageRepository.putString("Biometry", value)

    override fun clearProfile() {
        isAuthorized = false
        clientId = null
        sessionData = null
        password = emptyString
        pin = emptyString
        biometry = emptyString
    }

    override fun clear() {
        clearProfile()
        deviceId = emptyString
    }
}