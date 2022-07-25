package kg.optima.mobile.network.client

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kg.optima.mobile.network.client.ext.setBody
import kg.optima.mobile.network.client.utils.cast
import kg.optima.mobile.network.client.utils.castOrNull
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

class NetworkClientImpl(
    override val httpClient: HttpClient,
    private val json: Json,
) : NetworkClient {

    override suspend fun <Request, Response> request(
        baseUrl: String,
        path: String,
        body: Request?,
        serializer: KSerializer<Request>?,
        httpMethod: HttpMethod,
    ): Response? = doRequest(baseUrl, path, body, serializer, httpMethod).castOrNull()

    override suspend fun <Request, Response> request(
        baseUrl: String,
        path: String,
        body: Request?,
        serializer: KSerializer<Request>?,
        httpMethod: HttpMethod,
        defaultValue: Response,
    ): Response = doRequest(baseUrl, path, body, serializer, httpMethod).cast(defaultValue)

    private suspend fun <Request> doRequest(
        baseUrl: String,
        path: String,
        body: Request?,
        serializer: KSerializer<Request>?,
        httpMethod: HttpMethod
    ): Any {
        return httpClient.request {
            url {
                takeFrom(baseUrl)
                path(path)
                method = httpMethod
                contentType(ContentType.Application.Json)
                if (body != null && serializer != null) { // TODO send error if serializer is null
                    setBody(serializer, body, json)
                }
            }
        }
    }
}
