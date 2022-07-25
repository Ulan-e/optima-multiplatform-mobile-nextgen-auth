package kg.optima.mobile.storage.model.image

fun List<ImageData>.getImagesM() = map { it.imagesM }
fun List<ImageData>.getImagesXS() = map { it.imagesXS }
fun List<ImageData>.getImagesXL() = map { it.imagesXL }