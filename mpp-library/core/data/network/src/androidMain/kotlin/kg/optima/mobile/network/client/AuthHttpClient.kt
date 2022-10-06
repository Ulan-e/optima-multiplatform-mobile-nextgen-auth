package kg.optima.mobile.network.client

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*

actual object AuthHttpClient {
	actual fun get(config: HttpClientConfig<*>.() -> Unit) = HttpClient(OkHttp) {
		engine {
			config {
				AndroidOkHttpClient.initUnsafeSSL(this)
			}
		}
		config()
	}

}