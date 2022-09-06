package kg.optima.mobile.base.presentation

import co.touchlab.stately.concurrency.AtomicReference
import kg.optima.mobile.core.error.Failure
import kg.optima.mobile.core.navigation.ScreenModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * [E] - Entity. In parameter, receiving from Domain,
 **/
abstract class StateMachine<in E>(
	coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main,
) {
	protected val stateValue: AtomicReference<@UnsafeVariance State?> = AtomicReference(null)

	/**
	 * Common state for each screen. Use with sealed classes.
	 */
	private val _state = MutableSharedFlow<State?>()
	val state: SharedFlow<State?> = _state.asSharedFlow()

	private val coroutineScope = CoroutineScope(coroutineDispatcher + SupervisorJob())

	protected fun setState(newState: @UnsafeVariance State?) {
		coroutineScope.launch(Dispatchers.Main) {
			_state.emit(newState)
		}
	}

	internal suspend fun setLoading() {
		_state.emit(State.Loading)
	}

	// TODO perform error
	internal suspend fun setError(error: State.Error) {
		_state.emit(error)
	}

	// TODO perform error
	internal fun setError(failure: Failure) {
//        when (failure) {
//            is BaseFailure.ApiCodeFailure -> TODO()
//            BaseFailure.ApiDataFailure -> TODO()
//            is BaseFailure.ApiMessageFailure -> TODO()
//            BaseFailure.BadRequestException -> TODO()
//            BaseFailure.ClientRequestException -> TODO()
//            BaseFailure.Default -> TODO()
//            is BaseFailure.Message -> TODO()
//            BaseFailure.NotFoundFailure -> TODO()
//            BaseFailure.NullPointFailure -> TODO()
//            BaseFailure.RepositoryFailure -> TODO()
//            BaseFailure.SocketTimeoutException -> TODO()
//            BaseFailure.UnknownException -> TODO()
//            BaseFailure.UseCaseError -> TODO()
//        }
	}

	internal fun pop() = setState(State.Pop)

	fun resetState() = setState(null)

	abstract fun handle(entity: E)

	interface State {
		object Initial : State

		object Loading : State

		class Navigate(val screenModels: List<ScreenModel>) : State

		object Pop : State

		sealed interface Error : State {
			val error: String

			class BaseError(override val error: String) : Error
		}
	}
}