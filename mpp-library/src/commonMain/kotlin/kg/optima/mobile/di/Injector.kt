package kg.optima.mobile.di

import kg.optima.mobile.auth.AuthFactory
//import kg.optima.mobile.network.NetworkFactory
import kg.optima.mobile.storage.StorageFactory
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

object Injector {

	private val commonModules = listOf(
//		NetworkFactory.module,
		StorageFactory.module
	)

	private val featureModules = listOf(
		AuthFactory.module
	)

	fun initKoin(appDeclaration: KoinAppDeclaration = {}): KoinApplication {
		return startKoin {
			appDeclaration()
			modules(commonModules + featureModules)
		}
	}

	// called by iOS
	fun initKoin() = initKoin {}
}