package kg.optima.mobile.network.profile

sealed interface NetworkProfile {
	val baseUrl: String

	class Debug(
		override val baseUrl: String
	) : NetworkProfile

	class Release(
		override val baseUrl: String
	) : NetworkProfile
}