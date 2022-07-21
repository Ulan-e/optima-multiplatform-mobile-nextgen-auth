package kg.optima.mobile.auth.data.api

import kg.optima.mobile.base.data.BaseApi
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.network.client.NetworkClient
import kg.optima.mobile.network.failure.Failure

abstract class AuthApi(
    networkClient: NetworkClient,
    baseUrl: String = ""
) : BaseApi(networkClient, baseUrl) {

    abstract suspend fun auth(): Either<Failure, String>
}