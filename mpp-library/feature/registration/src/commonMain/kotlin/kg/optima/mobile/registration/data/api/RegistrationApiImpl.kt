package kg.optima.mobile.registration.data.api

import io.ktor.http.*
import kg.optima.mobile.base.data.model.BaseDto
import kg.optima.mobile.network.client.NetworkClient
import kg.optima.mobile.registration.data.api.model.PhoneCheckDto
import kg.optima.mobile.registration.data.api.model.PhoneCheckRequest

class RegistrationApiImpl(
	networkClient: NetworkClient,
) : RegistrationApi(networkClient) {

	override suspend fun checkPhoneNumber(phoneCheckRequest: PhoneCheckRequest): BaseDto<PhoneCheckDto> =
		post(
			path = "vl/check-phone",
			headers = {
				append(HttpHeaders.AcceptLanguage, "ru-RU")
				append(HttpHeaders.UserAgent, userAgent())
			},
			request = phoneCheckRequest,
		)

	private fun userAgent(): String {
		return "Optima24/1.0 (Android; Samsung Galaxy S21 Ultra/000000000000000)"
	}

}