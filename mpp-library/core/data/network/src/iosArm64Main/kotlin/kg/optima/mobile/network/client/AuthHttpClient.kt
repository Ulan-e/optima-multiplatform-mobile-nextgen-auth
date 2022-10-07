package kg.optima.mobile.network.client

import io.ktor.client.*
import io.ktor.client.engine.ios.*

actual object AuthHttpClient {
	actual fun get(config: HttpClientConfig<*>.() -> Unit) = HttpClient(Ios) {
		engine {
			handleChallenge(TrustSelfSignedCertificate())
		}
	}
}