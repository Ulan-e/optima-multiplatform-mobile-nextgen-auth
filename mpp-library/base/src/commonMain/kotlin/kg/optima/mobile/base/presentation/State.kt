package kg.optima.mobile.base.presentation

import co.touchlab.stately.concurrency.AtomicReference
import kg.optima.mobile.core.error.Failure
import kg.optima.mobile.core.navigation.ScreenModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * [E] - Entity. In parameter, receiving from Domain,
 **/
abstract class State<in E>(
	coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main,
) {
	protected val stateValue: AtomicReference<@UnsafeVariance StateModel?> = AtomicReference(null)

	/**
	 * Common state for each screen. Use with sealed classes.
	 */
	private val _stateFlow = MutableSharedFlow<StateModel?>()
	val stateFlow: SharedFlow<StateModel?> = _stateFlow.asSharedFlow()

	private val coroutineScope = CoroutineScope(coroutineDispatcher + SupervisorJob())

	protected fun setStateModel(newState: @UnsafeVariance StateModel?) {
		coroutineScope.launch {
			_stateFlow.emit(newState)
		}
	}

	internal fun setLoading() {
		_stateFlow.tryEmit(StateModel.Loading)
	}

	// TODO perform error
	internal suspend fun setError(error: StateModel.Error) {
		_stateFlow.emit(error)
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

	internal fun pop() = setStateModel(StateModel.Pop)

	abstract fun handle(entity: E)

	interface StateModel {
		object Initial : StateModel

		object Loading : StateModel

		class Navigate(val screenModels: List<ScreenModel>) : StateModel {
			constructor(screenModel: ScreenModel) : this(listOf(screenModel))
		}

		object Pop : StateModel

		sealed interface Error : StateModel {
			val error: String

			class BaseError(override val error: String) : Error
		}
	}
}