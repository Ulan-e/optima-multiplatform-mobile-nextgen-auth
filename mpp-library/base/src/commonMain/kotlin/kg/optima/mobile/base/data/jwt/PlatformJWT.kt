package kg.optima.mobile.base.data.jwt

expect object PlatformJWT {

    /**
     * [token] - jwt token,
     * [parameter] - key name,
     * decode jwt token and returns parameter value by key
     **/
    fun getParamByKey(token: String, parameter: JwtParameter): String

}