package kg.optima.mobile.feature.auth.component

import kg.optima.mobile.feature.auth.model.GrantType

interface AuthPreferences {

    companion object {
        const val AUTHORIZED = "AUTHORIZED"
        const val CLIENT_ID = "USER_ID"
        const val DEVICE_ID = "DEVICE_ID"
        const val FIRST_START = "FIRST_START"
        const val SESSION_DATA = "SESSION_DATA"
        const val USER_INFO = "USER_INFO"
        const val PIN_ATTEMPTS = "PIN_ATTEMPTS"
        const val TOUCH_ATTEMPTS = "TOUCH_ATTEMPTS"
    }

    var clientId: String?
    var isAuthorized: Boolean
    var deviceId: String
    var sessionData: SessionData?
    var userInfo: UserInfo?

    var password: String
    var pin: String
    var pinAttempts: Int
    var touchAttempts: Int
    var biometry: String

    val grantTypes: List<GrantType> get() {
        val grantTypes = mutableListOf<GrantType>()
        if (isAuthorized) {
            grantTypes.add(GrantType.Password)
            if (pin.isNotBlank()) grantTypes.add(GrantType.Pin)
            if (biometry.isNotBlank()) grantTypes.add(GrantType.Biometry)
        }
        return grantTypes
    }

    fun clearProfile()

    fun clear()
}