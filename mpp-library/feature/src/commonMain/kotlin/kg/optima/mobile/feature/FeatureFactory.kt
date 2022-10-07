package kg.optima.mobile.feature

import kg.optima.mobile.base.di.Factory
import kg.optima.mobile.feature.auth.component.AuthPreferences
import kg.optima.mobile.feature.auth.component.AuthPreferencesImpl
import org.koin.dsl.module

object FeatureFactory : Factory {
	override val module = module {
		factory<AuthPreferences> { AuthPreferencesImpl(storageRepository = get()) }
	}
}