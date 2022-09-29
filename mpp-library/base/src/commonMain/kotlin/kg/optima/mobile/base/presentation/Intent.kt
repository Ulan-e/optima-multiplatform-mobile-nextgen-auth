package kg.optima.mobile.base.presentation

import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.presentation.permissions.Permission
import kg.optima.mobile.core.error.Failure
import kg.optima.mobile.core.navigation.ScreenModel
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

	open fun nextScreenModel(nextScreenModel: ScreenModel) {
		state.setStateModel(State.StateModel.Navigate(nextScreenModel))
	}

	open fun pop() =
		state.pop()

	fun requestPermission(permission: Permission) = requestPermissions(listOf(permission))

	open fun requestPermissions(permissions: List<Permission>) {
		coroutineScope.launch(handler) {
			state.setStateModel(State.StateModel.RequestPermissions(permissions))
		}
	}

	open fun customPermissionRequired(permissions: List<Permission>) {
		coroutineScope.launch(handler) {
			state.setStateModel(State.StateModel.CustomPermissionRequired(
				text = "Чтобы вы могли зарегистрироваться приложению нужен " +
						"доступ к ${permissions.text()}",
				permissions = permissions,
			))
		}
	}

	protected fun launchOperation(
		withLoading: Boolean = true,
		operation: suspend () -> Either<Failure, E>,
	): Job {
		return coroutineScope.launch(handler) {
			if (withLoading) state.setLoading()
			delay(200)
			operation().fold(
				fnL = { err -> state.setError(State.StateModel.Error.BaseError(err.message)) },
				fnR = { model -> state.handle(model) }
			)
		}
	}

	private val Permission.text: String
		get() = when (this) {
			Permission.Camera -> "камере"
			Permission.Storage -> "внутреннему хранилищу"
		}

	protected fun List<Permission>.text(): String {
		return StringBuilder().apply {
			this@text.forEach { permission ->
				append(permission.text)
				if (permission != this@text.last()) append(", ")
			}
		}.toString()
	}
}