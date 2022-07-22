//package kg.optima.mobile.network.di
//
//import io.ktor.client.*
//import io.ktor.client.engine.cio.*
//import io.ktor.client.plugins.*
//import io.ktor.client.plugins.contentnegotiation.*
//import io.ktor.client.plugins.logging.*
//import io.ktor.client.plugins.websocket.*
//import io.ktor.client.statement.*
//import io.ktor.http.*
//import io.ktor.serialization.kotlinx.*
//import io.ktor.serialization.kotlinx.json.*
//import io.ktor.util.*
//import kg.optima.mobile.network.client.NetworkClient
//import kg.optima.mobile.network.client.NetworkClientImpl
//import kg.optima.mobile.network.failure.NetworkFailure
//import kg.optima.mobile.network.logger.KtorLogger
//import kotlinx.serialization.json.Json
//
//fun provideNetworkClient(httpClient: HttpClient, json: Json): NetworkClient {
//    return NetworkClientImpl(httpClient, json)
//}
//
//fun provideJson() = Json {
//    isLenient = true
//    prettyPrint = true
//    ignoreUnknownKeys = true
//    encodeDefaults = true
//}
//
//private const val TIME_OUT = 30000L
//fun provideHttpClient(
//    json: Json,
//    httpLogger: Logger = KtorLogger(),
//    networkFailure: NetworkFailure,
//    params: Map<String, String>
//): HttpClient {
//    return HttpClient(CIO) {
//        expectSuccess = true // TODO review
//
//        install(ContentNegotiation) {
//            json(json)
//        }
//        defaultRequest {
//            params.forEach { headers.appendIfNameAndValueAbsent(it.key, it.value) }
//        }
//        install(WebSockets) {
//            contentConverter = KotlinxWebsocketSerializationConverter(Json)
//        }
//        install(HttpTimeout) {
//            connectTimeoutMillis = TIME_OUT
//            socketTimeoutMillis = TIME_OUT
//            requestTimeoutMillis = TIME_OUT
//        }
//        install(Logging) {
//            level = LogLevel.ALL
//            logger = httpLogger
//        }
////        install(HttpRequestRetry) { // TODO uncomment if needed
////            retryOnServerErrors(maxRetries = 5)
////            exponentialDelay()
////        }
//        HttpResponseValidator {
//            handleResponseExceptionWithRequest { cause, request ->
//                when (cause) {
//                    is ClientRequestException -> {
//                        throw getError(cause, networkFailure)
//                    }
//                    else -> {
//                        throw networkFailure.getUnknownException()
//                    }
//                }
//            }
//        }
//    }
//}
//
//private suspend fun getError(
//    error: ClientRequestException,
//    networkFailure: NetworkFailure,
//): Throwable {
//    return try {
//        when (error.response.status) {
//            HttpStatusCode.NotFound -> {
//                networkFailure.getNotFoundFailure()
//            }
//            else -> {
//                //        crashLogger.recordException(failure)
//                val errorResponse = error.response.bodyAsText()
//                networkFailure.getBaseFailure(errorResponse)
//            }
//        }
//    } catch (e: Throwable) {
//        networkFailure.getUnknownException()
//    }
//}