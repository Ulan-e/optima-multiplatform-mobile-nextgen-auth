package kg.optima.mobile.registration.data.api

import kg.optima.mobile.base.data.BaseApi
import kg.optima.mobile.base.data.model.BaseDto
import kg.optima.mobile.network.client.NetworkClient
import kg.optima.mobile.registration.data.api.model.*

abstract class RegistrationApi(
	networkClient: NetworkClient,
) : BaseApi(networkClient) {
	override val baseUrl: String = "https://api.optimabank.kg"

	abstract suspend fun checkPhoneNumber(phoneCheckRequest: PhoneCheckRequest): BaseDto<PhoneCheckDto>

	abstract suspend fun checkSmsCode(
		codeCheckRequest: CodeCheckRequest,
		referenceId: String
	) : BaseDto<CheckCodeDto>

	abstract suspend fun getQuestions() : BaseDto<List<QuestionDto>>
}