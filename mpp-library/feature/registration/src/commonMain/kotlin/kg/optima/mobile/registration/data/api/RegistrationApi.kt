package kg.optima.mobile.registration.data.api

import kg.optima.mobile.base.data.BaseApi
import kg.optima.mobile.base.data.model.BaseDto
import kg.optima.mobile.network.client.NetworkClient
import kg.optima.mobile.registration.data.api.model.PhoneCheckDto
import kg.optima.mobile.registration.data.api.model.PhoneCheckRequest

abstract class RegistrationApi(
	networkClient: NetworkClient,
) : BaseApi(networkClient) {
	override val baseUrl: String = "https://api.optimabank.kg"

	abstract suspend fun checkPhoneNumber(phoneCheckRequest: PhoneCheckRequest): BaseDto<PhoneCheckDto>
}