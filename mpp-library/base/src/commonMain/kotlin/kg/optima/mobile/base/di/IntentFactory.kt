package kg.optima.mobile.base.di

import kg.optima.mobile.base.presentation.IntentHandler
import kg.optima.mobile.base.presentation.StateMachine
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

object IntentFactory : KoinComponent {

	inline fun <reified SM, E> stateMachine(
		vararg parameters: Any? = arrayOf(null)
	): SM where SM : StateMachine<E> {
		val sm: SM by inject { parametersOf(parameters) }
		return sm
	}

	inline fun <reified IH : IntentHandler<I, E>, reified SM : StateMachine<E>, I, E> intentHandler(): IH {
		val ih: IH by inject { parametersOf(stateMachine<SM, E>()) }
		return ih
	}

}