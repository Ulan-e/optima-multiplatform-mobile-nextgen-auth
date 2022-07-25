package kg.optima.mobile.base.presentation

import co.touchlab.stately.concurrency.AtomicReference
import kg.optima.mobile.base.presentation.utils.asCommonFlow
import kg.optima.mobile.core.PlatformViewModel
import kg.optima.mobile.core.error.Failure
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filterNotNull

/**
 * [DomainModel] - In parameter, receiving from UseCase,
 * [State] - Out parameter, sending to StateFlow.
 * */
abstract class StateMachine<in DomainModel, out State>(
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main,
) : PlatformViewModel() {
    /**
     * Common state for each screen. Use with sealed classes.
     */
    private val _state = MutableSharedFlow<State?>()
    protected val stateValue: AtomicReference<@UnsafeVariance State?> = AtomicReference(null)

    /**
     * for iOS
     */
    val state = _state.filterNotNull().asCommonFlow()

    /**
     * for Android
     */
    val stateFlow = _state.filterNotNull()

    /**
     * Loading status changing by [StateMachine.launchOperation].
     */
    internal val _status = MutableSharedFlow<Status>(1)
    val status = _status.filterNotNull().asCommonFlow()

    // TODO combine status and error
    private val _error = MutableSharedFlow<Failure?>(1)
    val error = _error.filterNotNull().asCommonFlow()

    private val coroutineScope = CoroutineScope(coroutineDispatcher + SupervisorJob())

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

    protected fun setState(newState: @UnsafeVariance State) {
        coroutineScope.launch {
            stateValue.set(newState)
            _state.emit(newState)
        }
    }

    abstract fun handle(model: DomainModel)

}