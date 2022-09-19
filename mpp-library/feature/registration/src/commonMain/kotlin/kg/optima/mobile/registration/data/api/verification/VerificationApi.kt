package kg.optima.mobile.registration.data.api.verification

import kg.optima.mobile.base.data.BaseApi
import kg.optima.mobile.base.data.model.BaseDto
import kg.optima.mobile.network.client.NetworkClient
import kg.optima.mobile.registration.data.api.model.*

abstract class VerificationApi(
    networkClient: NetworkClient
) : BaseApi(networkClient) {

    override val baseUrl: String = "https://api.optimabank.kg"

    abstract suspend fun checkPhoneNumber(
        phoneCheckRequest: PhoneCheckRequest
    ): BaseDto<PhoneCheckDto>

    abstract suspend fun checkSmsCode(
        codeCheckRequest: CodeCheckRequest,
        referenceId: String
    ): BaseDto<CheckCodeDto>

    abstract suspend fun verifyClient(
        referenceId: String,
        sessionId: String,
        livenessResult: String,
        accessToken: String,
        personId: String,
        verifyClientRequest: VerifyClientRequest
    ): BaseDto<VerifyClientDto>
}