package kg.optima.mobile.auth.data.component

import kg.optima.mobile.base.data.PlatformDate
import kg.optima.mobile.base.data.jwt.JwtParameter
import kg.optima.mobile.base.data.jwt.PlatformJWT
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.storage.StorageRepository
import kg.optima.mobile.storage.cache.RuntimeCache

class FeatureAuthComponentImpl(
    private val storageRepository: StorageRepository,
    private val runtimeCache: RuntimeCache,
) : FeatureAuthComponent {

    override var clientId: String?
        get() = storageRepository.getString(FeatureAuthComponent.CLIENT_ID)
        set(value) = storageRepository.putString(FeatureAuthComponent.CLIENT_ID, value.orEmpty())

    override var refreshToken: String?
        get() = storageRepository.getString(FeatureAuthComponent.REFRESH_TOKEN)
        set(value) = storageRepository.putString(FeatureAuthComponent.REFRESH_TOKEN, value.orEmpty())

    override var isAuthorized: Boolean
        get() = storageRepository.getBoolean(FeatureAuthComponent.AUTHORIZED, false)
        set(value) = storageRepository.putBoolean(FeatureAuthComponent.AUTHORIZED, value)

    override var deviceId: String
        get() = storageRepository.getString(
            key = FeatureAuthComponent.DEVICE_ID,
            defaultValue = PlatformDate.getTimeMills().also { id -> deviceId = id }
        )
        set(value) = storageRepository.putString(FeatureAuthComponent.DEVICE_ID, value)

    override var token: String?
        get() = storageRepository.getString(FeatureAuthComponent.TOKEN)
        set(value) = storageRepository.putString(FeatureAuthComponent.TOKEN, value.orEmpty())

    override var pin: String
        get() = storageRepository.getString("PIN", emptyString)
        set(value) = storageRepository.putString("PIN", value)

    override fun saveToken(token: String?) {
//        clientId = if (token != null) {
//            PlatformJWT.getParamByKey(token, JwtParameter.USER_ID)
//        } else {
//            null
//        }
        storageRepository.putString(FeatureAuthComponent.TOKEN, token.orEmpty())
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