package org.example.library

import dev.icerock.moko.network.exceptionfactory.HttpExceptionFactory
import dev.icerock.moko.network.exceptionfactory.parser.ErrorExceptionParser
import dev.icerock.moko.network.exceptionfactory.parser.ValidationExceptionParser
import dev.icerock.moko.network.features.ExceptionFeature
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

fun createHttpClient(
    json: Json,
    mock: MockRequestHandler
): HttpClient = createHttpClient(
    json = json,
    config = {},
    mock = mock
)

fun createHttpClient(
    json: Json,
    config: HttpClientConfig<MockEngineConfig>.() -> Unit,
    mock: MockRequestHandler
): HttpClient {
    return HttpClient(MockEngine) {
        engine {
            addHandler(mock)
        }

        expectSuccess = false
        install(ExceptionFeature) {
            exceptionFactory = HttpExceptionFactory(
                defaultParser = ErrorExceptionParser(json),
                customParsers = mapOf(
                    HttpStatusCode.UnprocessableEntity.value to ValidationExceptionParser(json)
                )
            )
        }

        config()
    }
}
