package kg.optima.mobile.di

import kg.optima.mobile.auth.AuthFeatureFactory
import kg.optima.mobile.common.CommonFeatureFactory
import kg.optima.mobile.feature.FeatureFactory
import kg.optima.mobile.network.NetworkFactory
import kg.optima.mobile.registration.RegistrationFeatureFactory
import kg.optima.mobile.storage.StorageFactory
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

object Injector {

	private val commonModules: List<Module> = listOf(
		NetworkFactory.module,
		StorageFactory.module,
	)

	private val featureModules: List<Module> = listOf(
		FeatureFactory.module,
		AuthFeatureFactory.module,
		CommonFeatureFactory.module,
		RegistrationFeatureFactory.module,
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