package kg.optima.mobile.base.data.jwt

actual object PlatformJWT {
	/**
	 * [token] - jwt token,
	 * [parameter] - key name,
	 * decode jwt token and returns parameter value by key
	 **/
	actual fun getParamByKey(
		token: String,
		parameter: JwtParameter
	): String {
		return ""
	}

}