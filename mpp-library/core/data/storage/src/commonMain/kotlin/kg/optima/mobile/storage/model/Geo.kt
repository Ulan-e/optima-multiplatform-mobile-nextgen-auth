package kg.optima.mobile.storage.model

import kotlinx.serialization.Serializable

@Serializable
class Geo(
    val id: Long,
    val titleRu: String,
    val slug: String,
    val latitude: Double,
    val longitude: Double,
    val lowerCornerLat: Double,
    val lowerCornerLon: Double,
    val upperCornerLat: Double,
    val upperCornerLon: Double,
    val zoom: Float,
    val cityCode: String,
    val bankCityCode: Long,
)