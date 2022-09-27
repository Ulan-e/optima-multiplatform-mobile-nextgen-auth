package kg.optima.mobile.common.domain.model

import kg.optima.mobile.feature.auth.model.GrantType

class LaunchInfo(
	val isAuthorized: Boolean,
	val clientId: String?,
	val grantTypes: List<GrantType>,
)