package kg.optima.mobile.auth.data.component

import kg.optima.mobile.base.data.PlatformDate
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.storage.StorageRepository
import kg.optima.mobile.storage.cache.RuntimeCache

class AuthPreferencesImpl(
    private val storageRepository: StorageRepository,
    private val runtimeCache: RuntimeCache,
) : AuthPreferences {

    override var clientId: String?
        get() = storageRepository.getString(AuthPreferences.CLIENT_ID)
        set(value) = storageRepository.putString(AuthPreferences.CLIENT_ID, value.orEmpty())

    override var refreshToken: String?
        get() = storageRepository.getString(AuthPreferences.REFRESH_TOKEN)
        set(value) = storageRepository.putString(AuthPreferences.REFRESH_TOKEN, value.orEmpty())

    override var isAuthorized: Boolean
        get() = storageRepository.getBoolean(AuthPreferences.AUTHORIZED, false)
        set(value) = storageRepository.putBoolean(AuthPreferences.AUTHORIZED, value)

    override var deviceId: String
        get() = storageRepository.getString(
            key = AuthPreferences.DEVICE_ID,
            defaultValue = PlatformDate.getTimeMills().also { id -> deviceId = id }
        )
        set(value) = storageRepository.putString(AuthPreferences.DEVICE_ID, value)

    override var token: String?
        get() = storageRepository.getString(AuthPreferences.TOKEN)
        set(value) = storageRepository.putString(AuthPreferences.TOKEN, value.orEmpty())

    override var password: String
        get() = storageRepository.getString("PASSWORD", emptyString)
        set(value) = storageRepository.putString("PASSWORD", value)

    override var pin: String
        get() = storageRepository.getString("PIN", emptyString)
        set(value) = storageRepository.putString("PIN", value)

    override var biometry: String
        get() = storageRepository.getString("Biometry", emptyString)
        set(value) = storageRepository.putString("Biometry", value)

    override fun saveToken(token: String?) {
//        clientId = if (token != null) {
//            PlatformJWT.getParamByKey(token, JwtParameter.USER_ID)
//        } else {
//            null
//        }
        storageRepository.putString(AuthPreferences.TOKEN, token.orEmpty())
    }

    override fun clearProfile() {
        isAuthorized = false
        saveToken(null)
        clientId = null
        refreshToken = null
        runtimeCache.setProfile(null)
    }

    override fun clear() {
        saveToken(null)
        refreshToken = null
        isAuthorized = false
    }
}