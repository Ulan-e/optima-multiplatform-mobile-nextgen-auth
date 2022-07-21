package kg.optima.mobile.base.data

import io.ktor.http.*
import kg.optima.mobile.network.client.NetworkClient
import kotlinx.serialization.KSerializer


abstract class BaseApi(
    private val networkClient: NetworkClient,
    private val baseUrl: String
) {
    suspend fun <T, R> request(
        path: String,
        body: T,
        serializer: KSerializer<T>,
        httpMethod: HttpMethod = HttpMethod.Post,
    ): R? {
        return networkClient.request(baseUrl, path, body, serializer, httpMethod)
    }

    suspend fun <T, R> request(
        path: String,
        body: T,
        serializer: KSerializer<T>,
        httpMethod: HttpMethod = HttpMethod.Post,
        defaultValue: R
    ): R {
        return networkClient.request(baseUrl, path, body, serializer, httpMethod, defaultValue)
    }
}