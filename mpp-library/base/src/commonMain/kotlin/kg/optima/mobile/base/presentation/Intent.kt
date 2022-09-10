package kg.optima.mobile.base.presentation

import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.core.error.Failure
import kotlinx.coroutines.*
import org.koin.core.component.KoinComponent

/**
 * [E] - Entity, In parameter coming from Domain,
 **/
abstract class Intent<in E>(
	coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main,
) : KoinComponent {

	private val coroutineScope = CoroutineScope(coroutineDispatcher)

	private val handler = CoroutineExceptionHandler { _, exception ->
		// TODO log exception to Crashlytics
		println("State CoroutineExceptionHandler got $exception")
	}

	protected abstract val state: State<E>

	open fun pop() = state.pop()

	protected fun launchOperation(
		operation: suspend () -> Either<Failure, E>,
	): Job {
		return coroutineScope.launch(handler) {
			state.setLoading()
			operation().fold(
				fnL = { err -> state.setError(State.StateModel.Error.BaseError(err.message)) },
				fnR = { model -> state.handle(model) }
			)
			delay(100)
		}
	}
}