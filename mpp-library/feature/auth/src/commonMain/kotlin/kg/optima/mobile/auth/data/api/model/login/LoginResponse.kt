package kg.optima.mobile.auth.data.api.model.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LoginResponse(
	@SerialName("AccessToken")
	val accessToken: String?,

	@SerialName("StartDateTime")
	val startDateTime: String?,

	@SerialName("LastUpdate")
	val lastUpdate: String?,

	@SerialName("User")
	val userInfo: UserInfo,

	@SerialName("Duration")
	val sessionDuration: Int,
)

@Serializable
class UserInfo(
	@SerialName("Id")
	val id: String?,

	@SerialName("BankId")
	val bankId: String?,

	@SerialName("FirstName")
	val firstName: String?,

	@SerialName("LastName")
	val lastName: String?,

	@SerialName("MiddleName")
	val middleName: String?,

	@SerialName("FullName")
	val fullName: String?,

	@SerialName("Idn")
	val idn: String?,

	@SerialName("Sex")
	val sex: String?,

	@SerialName("Login")
	val login: String?,

	@SerialName("Address")
	val address: String?,

	@SerialName("AutoEncrypt")
	val autoEncrypt: Boolean,

	@SerialName("MobilePhoneNumber")
	val phoneNumber: String?,

	@SerialName("ImageHash")
	val imageHash: String?,
)