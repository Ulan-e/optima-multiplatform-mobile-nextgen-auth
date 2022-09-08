package kg.optima.mobile.base.di

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf

/**
 * Koin can't collect [org.koin.dsl.ModuleDeclaration] to one module.
 * So we should create all module injects in [module] value.
 * But we can declare variables by [org.koin.core.component.inject] and use them in separate classes.
 **/
abstract class Factory : KoinComponent {
	abstract val module: Module

	inline fun <reified I, reified SM> create(
		parameter: Any? = null,
	): Product<I, SM> {
		val sm: SM by inject { parametersOf(parameter) }
		val ih: I by inject { parametersOf(sm) }
		return Product(ih, sm)
	}

	class Product<I, SM>(
		val intent: I,
		val stateMachine: SM,
	)
}