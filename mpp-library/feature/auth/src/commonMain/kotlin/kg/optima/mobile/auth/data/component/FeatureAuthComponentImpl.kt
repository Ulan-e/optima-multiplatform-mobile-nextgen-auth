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

    override var userId: String?
        get() = storageRepository.getString(FeatureAuthComponent.USER_ID, emptyString)
        set(value) {
            storageRepository.putString(FeatureAuthComponent.USER_ID, value.orEmpty())
        }
    override var profileId: String?
        get() = storageRepository.getString(FeatureAuthComponent.PROFILE_ID, emptyString)
        set(value) {
            storageRepository.putString(FeatureAuthComponent.PROFILE_ID, value.orEmpty())
        }

    override var refreshToken: String?
        get() = storageRepository.getString(FeatureAuthComponent.REFRESH_TOKEN, emptyString)
        set(value) {
            storageRepository.putString(FeatureAuthComponent.REFRESH_TOKEN, value.orEmpty())
        }

    override var isAuthorized: Boolean
        get() = storageRepository.getBoolean(FeatureAuthComponent.AUTHORIZED, false)
        set(value) {
            storageRepository.putBoolean(FeatureAuthComponent.AUTHORIZED, value)
        }

    override var deviceId: String
        get() = storageRepository.getString(FeatureAuthComponent.DEVICE_ID) ?: run {
            return PlatformDate.getTimeMills().toString().also { id -> deviceId = id }
        }
        set(value) {
            storageRepository.putString(FeatureAuthComponent.DEVICE_ID, value)
        }

    override fun saveToken(token: String?) {
        if (token == null) {
            userId = null
            profileId = null
        } else {
            userId = PlatformJWT.getParamByKey(token, JwtParameter.USER_ID)
            profileId = PlatformJWT.getParamByKey(token, JwtParameter.BASE_PROFILE_ID)
        }
        storageRepository.putString(FeatureAuthComponent.TOKEN, token.orEmpty())
    }

    override fun saveMobile(mobile: String?) {
        storageRepository.putString(FeatureAuthComponent.MOBILE, mobile.orEmpty())
    }

    override fun clearProfile() {
        isAuthorized = false
        saveToken(null)
        userId = null
        profileId = null
        refreshToken = null
        runtimeCache.setProfile(null)
    }

    override fun clear() {
        saveToken(null)
        refreshToken = null
        isAuthorized = false
    }
}