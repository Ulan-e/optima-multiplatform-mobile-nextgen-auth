package kg.optima.mobile.base.di

import org.koin.core.module.Module
import org.koin.dsl.ModuleDeclaration
import org.koin.dsl.module

/**
 * Koin can't collect [org.koin.dsl.ModuleDeclaration] to one module.
 * So we should create all module injects in [module] value.
 * But we can declare variables by [org.koin.core.component.inject] and use them in separate classes.
 * F.e. [kg.optima.mobile.base.di.IntentFactory].
 **/
interface Factory {
	val module: Module
}