package kg.optima.mobile.registration.data.api

import kg.optima.mobile.base.data.BaseApi
import kg.optima.mobile.base.data.model.BaseDto
import kg.optima.mobile.network.client.NetworkClient
import kg.optima.mobile.registration.data.api.model.CheckCodeDto
import kg.optima.mobile.registration.data.api.model.CodeCheckRequest
import kg.optima.mobile.registration.data.api.model.PhoneCheckDto
import kg.optima.mobile.registration.data.api.model.PhoneCheckRequest

abstract class RegistrationApi(
	networkClient: NetworkClient,
) : BaseApi(networkClient) {
	override val baseUrl: String = "https://api.optimabank.kg"

	abstract suspend fun checkPhoneNumber(phoneCheckRequest: PhoneCheckRequest): BaseDto<PhoneCheckDto>

	abstract suspend fun checkSmsCode(codeCheckRequest: CodeCheckRequest) : BaseDto<CheckCodeDto>
}