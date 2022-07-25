package kg.optima.mobile.storage.model.image

sealed class Image {
    class Local(val path: String) : Image()
    class Remote(
        val id: String,
        val imagesM: String,
        val imagesXS: String = "",
        val imagesXL: String,
    ) : Image()

    class Video(val url: String,
                val youtubeId: String,
                val urlImage: String) : Image()
    //TODO
    object Panorama : Image()

    fun isLocal() = this is Local
    fun getLocal() = this as Local

    fun isRemote() = this is Remote
    fun getRemote() = this as Remote

    fun isVideo() = this is Video
    fun getVideo() = this as Video

    fun isPanorama() = this is Panorama
    fun igetPanorama() = this as Panorama
}