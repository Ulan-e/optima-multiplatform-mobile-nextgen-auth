package kg.optima.mobile.network.di

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.charsets.*
import kg.optima.mobile.core.StringMap
import kg.optima.mobile.network.client.NetworkClient
import kg.optima.mobile.network.client.NetworkClientImpl
import kg.optima.mobile.network.failure.NetworkFailure
import kg.optima.mobile.network.header.DefaultHeader
import kg.optima.mobile.network.logger.KtorLogger
import kotlinx.serialization.json.Json

fun provideNetworkClient(httpClient: HttpClient, json: Json): NetworkClient {
    return NetworkClientImpl(httpClient, json)
}

fun provideSerializer() = KotlinxSerializer(
    Json {
        isLenient = true
        ignoreUnknownKeys = true
        explicitNulls = false
    }
)

fun provideJson() = Json {
    isLenient = true
    prettyPrint = true
    ignoreUnknownKeys = true
    encodeDefaults = true
}

private const val TIME_OUT = 60000L
fun provideHttpClient(
    kotlinxSerializer: KotlinxSerializer,
    httpLogger: Logger = KtorLogger(),
    networkFailure: NetworkFailure,
    params: StringMap,
): HttpClient {
    return HttpClient {
        install(JsonFeature) {
            serializer = kotlinxSerializer
            accept(ContentType.Application.FormUrlEncoded)
            accept(ContentType.Application.Json)
            accept(ContentType.Text.Plain)
            accept(ContentType.Text.Html)
        }
        install(DefaultHeader) {
            headers = { params }
        }
        HttpResponseValidator {
            handleResponseException { error ->
                print(error)
                when (error) {
                    is ClientRequestException -> {
                        throw getError(error, networkFailure)
                    }
                    else -> {
                        throw networkFailure.getUnknownException()
                    }
                }
            }
        }
        install(HttpTimeout) {
            connectTimeoutMillis = TIME_OUT
            socketTimeoutMillis = TIME_OUT
            requestTimeoutMillis = TIME_OUT
        }
        install(Logging) {
            level = LogLevel.ALL
            logger = httpLogger
        }
    }
}

private suspend fun getError(
    error: ClientRequestException,
    networkFailure: NetworkFailure,
): Throwable {
    return try {
        when (error.response.status) {
            HttpStatusCode.NotFound -> {
                networkFailure.getNotFoundFailure()
            }
            else -> {
                //        crashLogger.recordException(failure)
                val errorResponse = error.response.readText(Charsets.UTF_8)
                networkFailure.getBaseFailure(errorResponse)
            }
        }
    } catch (e: Throwable) {
        networkFailure.getUnknownException()
    }
}