package kg.optima.mobile.base.di

import org.koin.core.component.KoinComponent
import org.koin.core.module.Module

/**
 * Koin can't collect [org.koin.dsl.ModuleDeclaration] to one module.
 * So we should create all module injects in [module] value.
 * But we can declare variables by [org.koin.core.component.inject] and use them in separate classes.
 **/
interface Factory : ISCreator {
	val module: Module

	class Product<I, S>(
		val intent: I,
		val state: S,
	)
}