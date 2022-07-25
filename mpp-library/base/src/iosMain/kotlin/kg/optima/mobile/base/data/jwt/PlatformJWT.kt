package kg.optima.mobile.base.data.jwt

actual object PlatformJWT {

    actual fun getParamByKey(token: String, parameter: JwtParameter): String {
        return try {
            val jwt = cocoapods.JWTDecode.A0JWT.decodeWithJwt(token, null)
            jwt?.body()?.get(parameter.key)?.toString().orEmpty()
        } catch (e: Exception) {
            ""
        }
    }
}