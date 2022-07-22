//package kg.optima.mobile.network
//
//import kg.optima.mobile.network.di.provideHttpClient
//import kg.optima.mobile.network.di.provideJson
//import kg.optima.mobile.network.di.provideNetworkClient
//import kg.optima.mobile.network.failure.NetworkFailureImpl
//import org.koin.core.module.Module
//import org.koin.dsl.module
//
//object NetworkFactory {
//
//	val module: Module = module {
//		factory { provideJson() }
//		factory {
//			provideHttpClient(
//				json = get(),
//				networkFailure = NetworkFailureImpl(json = get()),
//				params = mapOf()
//			)
//		}
//		factory {
//			provideNetworkClient(httpClient = get(), json = get())
//		}
//	}
//}