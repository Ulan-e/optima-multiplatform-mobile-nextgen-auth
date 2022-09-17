package kg.optima.mobile.registration.data.api.registration

import io.ktor.http.*
import kg.optima.mobile.base.data.model.BaseDto
import kg.optima.mobile.network.client.NetworkClient
import kg.optima.mobile.registration.data.api.model.*

class RegistrationApiImpl(
    networkClient: NetworkClient,
) : RegistrationApi(networkClient) {

    override suspend fun getQuestions(): BaseDto<List<QuestionDto>> =
        get(
            path = "api/registration/questions",
            headers = {
                append(HttpHeaders.AcceptLanguage, "ru-RU")
            }
        )

    override suspend fun register(registrationRequest: RegistrationRequest): BaseDto<String> =
        post(
            path = "api/registration/remoteRegisterUser",
            headers = {
                append(HttpHeaders.AcceptLanguage, "ru-RU")
            },
            request = registrationRequest,
        )

}