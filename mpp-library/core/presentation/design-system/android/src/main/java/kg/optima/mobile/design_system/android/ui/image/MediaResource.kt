package kg.optima.mobile.design_system.android.ui.image

sealed interface MediaResource {
	class Local(
		val path: String
	) : MediaResource

	class Remote(
		val id: String,
		val imagesM: String,
		val imagesXS: String,
		val imagesXL: String,
	) : MediaResource

	class Video(
		val url: String,
		val youtubeId: String,
		val urlImage: String
	) : MediaResource

	fun isLocal() = this is Local
	fun getLocal() = this as Local

	fun isRemote() = this is Remote
	fun getRemote() = this as Remote

	fun isVideo() = this is Video
	fun getVideo() = this as Video
}