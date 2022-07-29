package kg.optima.mobile.network.header

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.util.*
import kg.optima.mobile.core.StringMap

//TODO refactor
class DefaultHeader(val headers: () -> StringMap) {

	class DefaultHeaderConfiguration {
		var headers: () -> StringMap = { emptyMap() }

		internal fun build(): DefaultHeader = DefaultHeader(headers)
	}


	companion object Feature : HttpClientFeature<DefaultHeaderConfiguration, DefaultHeader> {

		override val key: AttributeKey<DefaultHeader> = AttributeKey("DefaultHeaderConfiguration")

		override fun install(feature: DefaultHeader, scope: HttpClient) {
			scope.sendPipeline.intercept(HttpSendPipeline.State) {
				feature.headers().forEach {
					if (context.url.encodedPath.contains("zip", true)) {
						println()
					} else {
						if (it.value.isNotBlank()) {
							context.headers.append(it.key, it.value)
						}
					}
				}
			}
		}

		override fun prepare(block: DefaultHeaderConfiguration.() -> Unit): DefaultHeader {
			println()
			return DefaultHeaderConfiguration().apply(block).build()
		}
	}

}