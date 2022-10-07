package kg.optima.mobile.feature.auth.component

@kotlinx.serialization.Serializable
data class UserInfo(
    val id: String?,
    val bankId: String?,
    val firstName: String?,
    val lastName: String?,
    val middleName: String?,
    val fullName: String?,
    val idn: String?,
    val sex: String?,
    val login: String?,
    val address: String?,
    val autoEncrypt: Boolean,
    val phoneNumber: String?,
    val imageHash: String?,
)