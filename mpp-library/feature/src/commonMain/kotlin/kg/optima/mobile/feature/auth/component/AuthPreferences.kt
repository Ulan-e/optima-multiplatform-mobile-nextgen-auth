package kg.optima.mobile.feature.auth.component

import kg.optima.mobile.feature.auth.model.GrantType

interface AuthPreferences {

    companion object {
        const val AUTHORIZED = "AUTHORIZED"
        const val CLIENT_ID = "USER_ID"
        const val DEVICE_ID = "DEVICE_ID"
        const val FIRST_START = "FIRST_START"
        const val SESSION_DATA = "SESSION_DATA"
    }

    var clientId: String?
    var isAuthorized: Boolean
    var deviceId: String
    var sessionData: SessionData?

    var password: String
    var pin: String
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