package kg.optima.mobile.registration.data.api

import io.ktor.http.*
import kg.optima.mobile.base.data.model.BaseDto
import kg.optima.mobile.network.client.NetworkClient
import kg.optima.mobile.registration.data.api.model.*

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
//		return format(
//			format = "%s (%s; %s/%s)",
//			"Optima24/1.0",
//			PlatformInfo.os,
//			PlatformInfo.deviceModel,
//			"000000000000000"
//		)
	}

	override suspend fun checkSmsCode(
		codeCheckRequest: CodeCheckRequest,
		referenceId: String
	): BaseDto<CheckCodeDto> =
		post(
			path = "vl/check-code",
			headers = {
				append(HttpHeaders.AcceptLanguage, "ru-RU")
				append(HttpHeaders.UserAgent, userAgent())
				append("reference-id", referenceId)
			},
			request = codeCheckRequest,
		)

	override suspend fun getQuestions(): BaseDto<List<QuestionDto>> =
		post(
			path = "api/registration/questions",
			headers = {
				append(HttpHeaders.AcceptLanguage, "ru-RU")
				append(HttpHeaders.UserAgent, userAgent())
			},
			request = null,
		)

	override suspend fun register(registrationRequest: RegistrationRequest): BaseDto<String> =
		post(
			path = "vl/check-code",
			headers = {
				append(HttpHeaders.AcceptLanguage, "ru-RU")
				append(HttpHeaders.UserAgent, userAgent())
			},
			request = registrationRequest,
		)

}