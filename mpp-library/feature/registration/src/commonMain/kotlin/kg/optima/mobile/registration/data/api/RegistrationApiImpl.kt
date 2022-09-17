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

	override suspend fun checkSmsCode(codeCheckRequest: CodeCheckRequest, referenceId: String): BaseDto<CheckCodeDto> =
		post(
			path = "vl/check-code",
			headers = {
				append(HttpHeaders.AcceptLanguage, "ru-RU")
				append(HttpHeaders.UserAgent, userAgent())
				append("reference-id", referenceId)
			},
			request = codeCheckRequest,
		)

	override suspend fun verifyClient(
		referenceId: String,
		sessionId: String,
		livenessResult: String,
		accessToken: String,
		personId: String,
		verifyClientRequest: VerifyClientRequest
	): BaseDto<VerifyClientDto> =
		post(
			path = "vl/verify-client",
			headers = {
				append(HttpHeaders.AcceptLanguage, "ru-RU")
				append(HttpHeaders.UserAgent, userAgent())
				append("reference-id", referenceId)
				append("session-id", sessionId)
				append("liveness-result", livenessResult)
				append("access-token", accessToken)
				append("person-id", personId)
			},
			request = verifyClientRequest,
		)

}