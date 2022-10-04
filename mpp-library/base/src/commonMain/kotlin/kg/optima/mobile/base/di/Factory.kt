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

	inline fun <reified I, reified S> create(
		stateParameter: Any? = null,
		intentParameter: Any? = null
	): Product<I, S> {
		val state: S by inject { parametersOf(stateParameter) }
		val intent: I by inject { parametersOf(state, intentParameter) }
		return Product(intent, state)
	}

	inline fun <reified I, reified S> create() = create<I, S>(null, null)

	inline fun <reified I, reified S> createWithStateParam(stateParameter: Any? = null,) =
		create<I, S>(stateParameter, null)

	inline fun <reified I, reified S> createWithIntentParam(intentParameter: Any? = null) =
		create<I, S>(null, intentParameter)

	class Product<I, S>(
		val intent: I,
		val state: S,
	)
}