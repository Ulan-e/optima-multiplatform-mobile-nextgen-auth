package kg.optima.mobile.common.presentation.launch

import kg.optima.mobile.auth.domain.usecase.login.GrantType
import kg.optima.mobile.core.navigation.ScreenModel

sealed interface LaunchEntity {
	class OpenNextScreen(
		val screenModel: ScreenModel
	) : LaunchEntity

	class ClientInfo(
		val isAuthorized: Boolean,
		val clientId: String?,
		val grantTypes: List<GrantType>,
	) : LaunchEntity
}