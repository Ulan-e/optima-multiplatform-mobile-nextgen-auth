package kg.optima.mobile.base.presentation

import kg.optima.mobile.base.presentation.permissions.Permission

interface BaseEntity {
	object Initial : BaseEntity

	class RequestPermissions(
		val permissions: List<Permission>
	) : BaseEntity

	class RequestCustomPermissions(
		val text: String,
		val permissions: List<Permission>
	) : BaseEntity
}