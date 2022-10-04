package kg.optima.mobile.base.presentation

import co.touchlab.stately.concurrency.AtomicReference
import kg.optima.mobile.base.presentation.permissions.Permission
import kg.optima.mobile.core.error.Failure
import kg.optima.mobile.core.navigation.ScreenModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * [E] - Entity. In parameter, receiving from Domain,
 **/
abstract class BaseMppState<in E>(
	coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main,
) {
	protected val stateValue: AtomicReference<@UnsafeVariance StateModel?> = AtomicReference(null)

	/**
	 * Common state for each screen. Use with sealed classes.
	 **/
	private val _stateFlow = MutableSharedFlow<StateModel?>()

	/**
	 * For Android.
	 */
	val stateFlow: Flow<StateModel?> = _stateFlow.asSharedFlow()

	/**
	 * For iOS.
	 */
	val commonStateFlow: CommonFlow<StateModel?> = _stateFlow.asCommonFlow()

	private val coroutineScope = CoroutineScope(coroutineDispatcher + SupervisorJob())

	fun setStateModel(newState: @UnsafeVariance StateModel?) {
		coroutineScope.launch { _stateFlow.emit(newState) }
	}

	fun init() = setStateModel(StateModel.Initial)

	internal fun setLoading() = setStateModel(StateModel.Loading)

	// TODO perform error
	internal fun setError(error: StateModel.Error) = setStateModel(error)

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

		class RequestPermissions(
			val permissions: List<Permission>
		) : StateModel

		class CustomPermissionRequired(
			val text: String,
			val permissions: List<Permission>
		) : StateModel

		sealed interface Error : StateModel {
			val error: String

			class BaseError(override val error: String) : Error

			class ApiError(override val error: String) : Error
		}
	}
}