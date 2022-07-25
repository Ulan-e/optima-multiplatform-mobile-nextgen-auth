package kg.optima.mobile.base.data.jwt

import com.auth0.android.jwt.JWT

actual object PlatformJWT {
    actual fun getParamByKey(token: String, parameter: JwtParameter): String {
        return try {
            val jwt = JWT(token)
            jwt.claims[parameter.key]?.asString().orEmpty()
        } catch (e: Exception) {
            ""
        }

    }
}