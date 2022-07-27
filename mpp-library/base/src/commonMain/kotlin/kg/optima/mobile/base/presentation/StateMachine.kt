package kg.optima.mobile.base.presentation

import co.touchlab.stately.concurrency.AtomicReference
import kg.optima.mobile.base.presentation.StateMachine.State
import kg.optima.mobile.base.presentation.utils.asCommonFlow
import kg.optima.mobile.core.error.Failure
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

/**
 * [E] - Entity. In parameter, receiving from Domain,
 * [S] - State. Out parameter, sending to StateFlow.
 **/
abstract class StateMachine<in E, out S : State>(
	coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main,
) {
	protected val stateValue: AtomicReference<@UnsafeVariance S?> = AtomicReference(null)

	/**
	 * Common state for each screen. Use with sealed classes.
	 */
	private val _state = MutableSharedFlow<S?>()
	val state: SharedFlow<S?> = _state

	private val _status = MutableSharedFlow<Status>(1)
	val status: SharedFlow<Status> get() = _status

	private val _error = MutableSharedFlow<Failure?>(1)
	val error = _error.filterNotNull().asCommonFlow()

	private val coroutineScope = CoroutineScope(coroutineDispatcher + SupervisorJob())

	protected fun setState(newState: @UnsafeVariance S?) {
		coroutineScope.launch(Dispatchers.Main) {
			stateValue.set(newState)
			_state.emit(newState)
			_status.emit(Status.HIDE_LOADING)
		}
	}

	internal suspend fun setStatus(status: Status) {
		_status.emit(status)
	}

	internal fun setError(error: String) {
		_status.tryEmit(Status.HIDE_LOADING)
		_error.tryEmit(Failure.Message(error))
	}

	// TODO perform error
	internal fun setError(failure: Failure) {
		_status.tryEmit(Status.HIDE_LOADING)
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

	abstract fun handle(entity: E)

	interface State
}