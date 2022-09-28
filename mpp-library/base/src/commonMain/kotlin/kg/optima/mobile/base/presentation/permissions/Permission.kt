package kg.optima.mobile.base.presentation.permissions

enum class Permission {
	Camera,
	Storage;
}

enum class PermissionState {
	Accepted,
	Requested,
	Denied,
	DeniedAlways;
}