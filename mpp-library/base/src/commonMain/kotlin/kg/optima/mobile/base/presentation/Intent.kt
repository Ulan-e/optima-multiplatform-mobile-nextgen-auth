package kg.optima.mobile.base.presentation

import dev.icerock.moko.permissions.*
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.core.error.Failure
import kg.optima.mobile.core.navigation.ScreenModel
import kotlinx.coroutines.*
import org.koin.core.component.KoinComponent

/**
 * [E] - Entity, In parameter coming from Domain,
 **/
abstract class Intent<in E>(
	coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main,
	private val permissionsController: PermissionsController? = null
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

	open fun pop() = state.pop()

	open fun requestPermissions(permissions: List<Permission>) {
		coroutineScope.launch(handler) {
			val acceptedPermissions: MutableList<Permission> = mutableListOf()
			val customPermissionRequiredPermissions: MutableList<Permission> = mutableListOf()

			permissions.forEach { permission ->
				try {
					permissionsController?.getPermissionState(permission)
					permissionsController?.providePermission(permission)
					acceptedPermissions.add(permission)
				} catch (deniedAlwaysException: DeniedAlwaysException) {
					customPermissionRequiredPermissions.add(permission)
				} catch (deniedException: DeniedException) {
					customPermissionRequiredPermissions.add(permission)
				} catch (e: Exception) {
					print(e)
				}
			}

			state.setStateModel(State.StateModel.RequestPermissionResult(
				title = "Чтобы вы могли зарегистрироваться приложению нужен " +
						"доступ к ${customPermissionRequiredPermissions.text()}",
				acceptedPermissions = acceptedPermissions,
				customPermissionRequiredPermissions = customPermissionRequiredPermissions,
			))
		}
	}

	open fun requestPermission(permission: Permission) = requestPermissions(listOf(permission))

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

	fun List<Permission>.text(): String {
		return StringBuilder("к ").apply {
			this@text.forEach { permission ->
				append(permission.text)
				if (permission != this@text.last()) append(", ")
			}
		}.toString()
	}

	val Permission.text: String
		get() = when (this) {
			Permission.CAMERA -> "камере"
			Permission.GALLERY -> "галерее"
			Permission.STORAGE -> "внутреннему хранилищу"
			Permission.WRITE_STORAGE -> "внутреннему хранилищу"
			Permission.LOCATION -> "местоположению"
			Permission.COARSE_LOCATION -> "местоположению"
			else -> emptyString
		}
}