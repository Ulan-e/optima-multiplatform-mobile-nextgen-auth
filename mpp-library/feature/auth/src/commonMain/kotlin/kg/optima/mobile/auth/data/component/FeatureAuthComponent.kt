package kg.optima.mobile.auth.data.component

interface FeatureAuthComponent {

    companion object {
        const val AUTHORIZED: String = "Authorized"
        const val CLIENT_ID = "USER_ID"
        const val DEVICE_ID = "DEVICE_ID"
        const val FIRST_START = "FIRST_START"
        const val GEO = "GEO"
        const val PROFILE_ID = "PROFILE_ID"
        const val TOKEN = "TOKEN"
        const val REFRESH_TOKEN = "REFRESH_TOKEN"
    }

    var clientId: String?
    var refreshToken: String?
    var isAuthorized: Boolean
    var deviceId: String
    var token: String?

    var pin: String

    fun saveToken(token: String?)

    fun clearProfile()

    fun clear()

}