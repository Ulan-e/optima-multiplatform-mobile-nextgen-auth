package kg.optima.mobile.base.presentation

import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.presentation.IntentHandler.Intent
import kg.optima.mobile.core.error.Failure
import kotlinx.coroutines.*
import org.koin.core.component.KoinComponent

/**
 * [I] - Intent, In parameter using as param launching executing,
 * [E] - Entity, In parameter coming from Domain,
 **/
abstract class IntentHandler<in I : Intent, in E>(
	coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main,
) : KoinComponent {

	private val coroutineScope = CoroutineScope(coroutineDispatcher)

	private val handler = CoroutineExceptionHandler { _, exception ->
		// TODO log exception to Crashlytics
		println("StateMachine CoroutineExceptionHandler got $exception")
	}

	protected abstract val stateMachine: StateMachine<E>

	abstract fun dispatch(intent: I)

	open fun pop() = stateMachine.pop()

	protected fun launchOperation(
		operation: suspend () -> Either<Failure, E>,
	): Job {
		return coroutineScope.launch(handler) {
			stateMachine.setLoading()
			operation().fold(
				fnL = { err -> stateMachine.setError(StateMachine.State.Error.BaseError(err.message)) },
				fnR = { model -> stateMachine.handle(model) }
			)
			delay(100)
		}
	}

	interface Intent
}