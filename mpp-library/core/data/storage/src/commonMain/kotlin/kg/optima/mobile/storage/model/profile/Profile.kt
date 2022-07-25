package kg.optima.mobile.storage.model.profile

import kg.optima.mobile.storage.model.Geo
import kg.optima.mobile.storage.model.image.Image

data class Profile(
    val id: String,
    val firstName: String,
    val lastName: String,
    val title: String,
    val userid: String,
    val description: String,
    val avatar: Image?,
    val geo: Geo?,
    val items: List<ProfileMap>,
    val phone: String = "",
    val profile: String = "$firstName $lastName",
    val firstLetters: String = "${firstName.firstOrNull() ?: ""} ${lastName.firstOrNull() ?: ""}",
)