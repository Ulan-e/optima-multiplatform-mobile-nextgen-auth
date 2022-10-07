package kg.optima.mobile.network.client

import io.ktor.client.*

expect object AuthHttpClient {
	fun get(config: HttpClientConfig<*>.() -> Unit): HttpClient
}