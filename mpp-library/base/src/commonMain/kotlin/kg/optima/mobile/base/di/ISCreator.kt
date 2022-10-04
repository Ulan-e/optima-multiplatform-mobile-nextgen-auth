package kg.optima.mobile.base.di

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

interface ISCreator : KoinComponent

inline fun <reified I, reified S> ISCreator.create(
	stateParameter: Any? = null,
	intentParameter: Any? = null
): Factory.Product<I, S> {
	val state: S by inject { parametersOf(stateParameter) }
	val intent: I by inject { parametersOf(state, intentParameter) }
	return Factory.Product(intent, state)
}

inline fun <reified I, reified S> ISCreator.create() = create<I, S>(null, null)

inline fun <reified I, reified S> ISCreator.createWithStateParam(stateParameter: Any? = null) =
	create<I, S>(stateParameter, null)

inline fun <reified I, reified S> ISCreator.createWithIntentParam(intentParameter: Any? = null) =
	create<I, S>(null, intentParameter)