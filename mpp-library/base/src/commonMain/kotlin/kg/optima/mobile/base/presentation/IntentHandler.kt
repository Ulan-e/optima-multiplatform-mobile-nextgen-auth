package kg.optima.mobile.base.presentation

import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.presentation.IntentHandler.Intent
import kg.optima.mobile.core.error.Failure
import kotlinx.coroutines.*
import org.koin.core.component.KoinComponent

/**
 * [Intent] - In parameter using as params for launch executing,
 * [E] - In parameter coming from Domain,
 * [S] - Out parameter sending to StateFlow.
 **/
abstract class IntentHandler<in I : IntentHandler.Intent, in E, out S : StateMachine.State>(
	coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main,
) : KoinComponent {

	private val coroutineScope = CoroutineScope(coroutineDispatcher)

	private val handler = CoroutineExceptionHandler { _, exception ->
		// TODO log exception to Crashlytics
		println("StateMachine CoroutineExceptionHandler got $exception")
	}

	abstract val stateMachine: StateMachine<E, S>

	abstract fun dispatch(intent: I)

	protected fun <T> launchOperation(
		operation: suspend () -> Either<Failure, E>,
	): Job {
		return coroutineScope.launch(handler) {
			stateMachine.setStatus(Status.SHOW_LOADING)
			operation().fold(
				fnL = { err -> stateMachine.setError(err) },
				fnR = { model -> stateMachine.handle(model) }
			)
			delay(100)
		}
	}

	interface Intent
}