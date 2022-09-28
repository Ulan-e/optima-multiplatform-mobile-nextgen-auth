package kg.optima.mobile.registration.data.api.registration

import kg.optima.mobile.base.data.BaseApi
import kg.optima.mobile.base.data.model.BaseDto
import kg.optima.mobile.network.client.NetworkClient
import kg.optima.mobile.registration.data.api.model.*

abstract class RegistrationApi(
    networkClient: NetworkClient,
) : BaseApi(networkClient) {

    override val baseUrl: String = "https://test-cl-mapi.optima24.kg"

    abstract suspend fun getQuestions(): BaseDto<List<QuestionDto>>

    abstract suspend fun register(
        registrationRequest: RegistrationRequest
    ): BaseDto<ClientDetailsDto>
}