package kg.optima.mobile.registration.data

interface RegistrationRepository {
	fun checkPhoneNumber(phoneNumber: String)
}