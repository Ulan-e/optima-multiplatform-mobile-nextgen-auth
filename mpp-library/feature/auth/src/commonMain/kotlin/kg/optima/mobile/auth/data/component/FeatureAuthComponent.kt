package kg.optima.mobile.auth.data.component

interface FeatureAuthComponent {

    companion object {
        const val AUTHORIZED: String = "Authorized"
        const val MOBILE = "MOBILE"
        const val DEVICE_ID = "DEVICE_ID"
        const val FIRST_START = "FIRST_START"
        const val GEO = "GEO"
        const val USER_ID = "USER_ID"
        const val PROFILE_ID = "PROFILE_ID"
        const val TOKEN = "TOKEN"
        const val REFRESH_TOKEN = "REFRESH_TOKEN"
    }

    var userId: String?
    var profileId: String?
    var refreshToken: String?
    var isAuthorized: Boolean
    var deviceId: String

    fun saveToken(token: String?)

    fun saveMobile(mobile: String?)

    fun clearProfile()

    fun clear()

}