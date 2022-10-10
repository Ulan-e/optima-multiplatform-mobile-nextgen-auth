package kg.optima.mobile.base.presentation

import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.presentation.permissions.Permission
import kg.optima.mobile.core.error.Failure
import kotlinx.coroutines.*
import org.koin.core.component.KoinComponent

/**
 * [E] - Entity, In parameter coming from Domain,
 **/
abstract class UiIntent<in E : BaseEntity>(
	coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main,
) : KoinComponent {

	private val coroutineScope = CoroutineScope(coroutineDispatcher)

	private val handler = CoroutineExceptionHandler { _, exception ->
		// TODO log exception to Crashlytics
		println("State CoroutineExceptionHandler got $exception")
	}

	protected abstract val uiState: UiState<E>

	protected fun launchOperation(
		withLoading: Boolean = true,
		operation: suspend () -> Either<Failure, E>,
	): Job {
		return coroutineScope.launch(handler) {
			if (withLoading) uiState.setLoading()
			operation().fold(
				fnL = { err -> uiState.setError(UiState.Model.Error.BaseError(err.message)) },
				fnR = { model -> uiState.handle(model) }
			)
		}
	}

	open fun init() = uiState.handle(BaseEntity.Initial)

	fun pop() = uiState.pop()

	fun requestPermission(permission: Permission) = requestPermissions(listOf(permission))

	fun requestPermissions(permissions: List<Permission>) {
		uiState.handle(BaseEntity.RequestPermissions(permissions))
	}

	fun customPermissionRequired(permissions: List<Permission>) {
		coroutineScope.launch(handler) {
			val text = StringBuilder().apply {
				permissions.forEach { permission ->
					val text = when (permission) {
						Permission.Camera -> "камере"
						Permission.Storage -> "внутреннему хранилищу"
					}
					append(text)
					if (permission != permissions.last()) append(", ")
				}
			}.toString()
			uiState.handle(
				BaseEntity.RequestCustomPermissions(
					text = "Чтобы вы могли зарегистрироваться приложению нужен " +
							"доступ к $text",
					permissions = permissions,
				)
			)
		}
	}
}