package kg.optima.mobile.base.presentation

import co.touchlab.stately.concurrency.AtomicReference
import com.arkivanov.essenty.parcelable.Parcelable
import kg.optima.mobile.base.presentation.permissions.Permission
import kg.optima.mobile.core.error.Failure
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * [E] - Entity. In parameter, receiving from Domain.
 **/
abstract class UiState<in E : BaseEntity>(
	coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main,
) {
	protected val stateValue: AtomicReference<@UnsafeVariance Model?> = AtomicReference(null)

	/**
	 * Common state for each screen. Use with sealed classes.
	 **/
	private val _stateFlow = MutableSharedFlow<Model?>()

	/**
	 * For Android.
	 */
	val stateFlow: Flow<Model?> = _stateFlow.asSharedFlow()

	/**
	 * For iOS.
	 */
	val commonStateFlow: CommonFlow<Model?> = _stateFlow.asCommonFlow()

	private val coroutineScope = CoroutineScope(coroutineDispatcher + SupervisorJob())

	/**
	 * To use only with intent.
	 **/
	protected fun setStateModel(newState: @UnsafeVariance Model?) {
		coroutineScope.launch { _stateFlow.emit(newState) }
	}

	internal fun setLoading() = setStateModel(Model.Loading)

	// TODO perform error
	internal fun setError(error: Model.Error) = setStateModel(error)

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

	internal fun pop() = setStateModel(Model.Pop)

	abstract fun handle(entity: E)

	internal fun handle(baseEntity: BaseEntity) = when (baseEntity) {
		BaseEntity.Initial ->
			setStateModel(Model.Initial)
		is BaseEntity.RequestPermissions ->
			setStateModel(Model.RequestPermissions(baseEntity.permissions))
		is BaseEntity.RequestCustomPermissions ->
			setStateModel(Model.CustomPermissionRequired(baseEntity.text, baseEntity.permissions))
		else -> Unit
	}

	internal fun init() = setStateModel(Model.Initial)

	interface Model {
		object Initial : Model

		object Loading : Model

		interface Navigate : Model, Parcelable {
			val dropBackStack: Boolean get() = false
		}

		object Pop : Model

		class RequestPermissions(
			val permissions: List<Permission>
		) : Model

		class CustomPermissionRequired(
			val text: String,
			val permissions: List<Permission>
		) : Model

		sealed interface Error : Model {
			val error: String

			class BaseError(override val error: String) : Error

			class ApiError(override val error: String) : Error
		}
	}
}