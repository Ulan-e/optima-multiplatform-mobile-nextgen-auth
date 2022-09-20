package kg.optima.mobile.android.ui.base.permission

import kg.optima.mobile.base.presentation.permissions.Permission

fun interface PermissionController {
	fun requestPermissionResult(state: State)

	sealed interface State {
		object Accepted : State
		class DeniedAlways(val permissions: List<Permission>) : State {
			constructor(permission: Permission) : this(listOf(permission))
		}
	}
}