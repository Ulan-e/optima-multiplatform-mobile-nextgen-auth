package kg.optima.mobile.auth

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.http.*
import kg.optima.mobile.core.StringMap
import kg.optima.mobile.network.client.AuthHttpClient
import kg.optima.mobile.network.di.getError
import kg.optima.mobile.network.failure.NetworkFailure
import kg.optima.mobile.network.header.DefaultHeader
import kg.optima.mobile.network.logger.KtorLogger

private const val TIME_OUT = 60000L
fun provideAuthHttpClient(
	kotlinxSerializer: KotlinxSerializer,
	httpLogger: Logger = KtorLogger(),
	networkFailure: NetworkFailure,
	params: StringMap,
): HttpClient {
	return AuthHttpClient.get {
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
					is ClientRequestException -> throw getError(error, networkFailure)
					else -> throw networkFailure.getUnknownException()
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