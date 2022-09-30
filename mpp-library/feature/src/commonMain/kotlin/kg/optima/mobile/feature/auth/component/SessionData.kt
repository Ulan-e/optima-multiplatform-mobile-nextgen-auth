package kg.optima.mobile.feature.auth.component

@kotlinx.serialization.Serializable
data class SessionData(
	val accessToken: String,
	val startDateTime: String?,
	val lastUpdate: String?,
	val sessionDuration: Int,
)
