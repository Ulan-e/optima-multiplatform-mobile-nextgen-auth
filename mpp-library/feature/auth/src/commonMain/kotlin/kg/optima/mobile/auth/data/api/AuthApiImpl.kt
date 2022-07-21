package kg.optima.mobile.auth.data.api

import kg.optima.mobile.auth.data.api.model.AuthModel
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.network.client.NetworkClient
import kg.optima.mobile.network.failure.Failure

class AuthApiImpl(
    networkClient: NetworkClient
) : AuthApi(networkClient) {
    override suspend fun auth(): Either<Failure, String> {
        val model = AuthModel("")
        val response = request(
            path = "",
            body = model,
            serializer = AuthModel.serializer(),
            defaultValue = ""
        )
        return Either.Right("")
    }
}
