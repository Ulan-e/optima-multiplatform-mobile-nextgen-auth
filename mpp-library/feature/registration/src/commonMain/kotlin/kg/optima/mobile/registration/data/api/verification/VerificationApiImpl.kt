package kg.optima.mobile.registration.data.api.verification

import io.ktor.http.*
import kg.optima.mobile.base.data.model.BaseDto
import kg.optima.mobile.network.client.NetworkClient
import kg.optima.mobile.registration.data.api.model.*

class VerificationApiImpl(
    networkClient: NetworkClient
) : VerificationApi(networkClient) {

    override suspend fun checkPhoneNumber(
        phoneCheckRequest: PhoneCheckRequest
    ): BaseDto<PhoneCheckDto> = post(
        path = "vl/check-phone",
        headers = {
            append(HttpHeaders.AcceptLanguage, "ru-RU")
        },
        request = phoneCheckRequest
    )

    override suspend fun checkSmsCode(
        codeCheckRequest: CodeCheckRequest,
        referenceId: String
    ): BaseDto<CheckCodeDto> = post(
        path = "vl/check-code",
        headers = {
            append(HttpHeaders.AcceptLanguage, "ru-RU")
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
                append("reference-id", referenceId)
                append("session-id", sessionId)
                append("liveness-result", livenessResult)
                append("access-token", accessToken)
                append("person-id", personId)
            },
            request = verifyClientRequest,
        )
}