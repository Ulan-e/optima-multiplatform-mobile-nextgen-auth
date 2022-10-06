package kg.optima.mobile.registration.data.api.registration

import kg.optima.mobile.base.data.model.BaseDto
import kg.optima.mobile.network.client.NetworkClient
import kg.optima.mobile.registration.data.api.model.ClientDetailsDto
import kg.optima.mobile.registration.data.api.model.QuestionDto
import kg.optima.mobile.registration.data.api.model.RegistrationRequest

class RegistrationApiImpl(
	networkClient: NetworkClient,
) : RegistrationApi(networkClient) {

	override suspend fun getQuestions(): BaseDto<List<QuestionDto>> =
		get(path = "api/registration/questions")

	override suspend fun register(registrationRequest: RegistrationRequest): BaseDto<ClientDetailsDto> =
		post(
			path = "api/registration/remoteRegisterUser",
			request = registrationRequest,
		)

}