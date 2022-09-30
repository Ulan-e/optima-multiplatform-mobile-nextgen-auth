package kg.optima.mobile.network

import io.ktor.http.*
import kg.optima.mobile.network.di.provideHttpClient
import kg.optima.mobile.network.di.provideJson
import kg.optima.mobile.network.di.provideNetworkClient
import kg.optima.mobile.network.di.provideSerializer
import kg.optima.mobile.network.failure.NetworkFailureImpl
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

object NetworkFactory {

	val module: Module = module {
		factory { provideJson() }
        factory { provideSerializer() }
		factory() {
			provideHttpClient(
                kotlinxSerializer = get(),
				networkFailure = NetworkFailureImpl(json = get()),
				params = mapOf(
					HttpHeaders.UserAgent to "Optima24/2.10.3 (Android/12; Samsung SM-G991B/vbeb8u4kz7ooj99o)"
				)
			)
		}
		factory() {
			provideNetworkClient(httpClient = get(), json = get())
		}
	}
}