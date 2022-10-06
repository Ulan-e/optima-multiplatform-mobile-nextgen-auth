package kg.optima.mobile.common.presentation.launch

import kg.optima.mobile.core.navigation.ScreenModel
import kg.optima.mobile.feature.auth.model.GrantType

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