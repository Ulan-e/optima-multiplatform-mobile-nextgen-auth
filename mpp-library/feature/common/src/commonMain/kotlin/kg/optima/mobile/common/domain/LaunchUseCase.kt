package kg.optima.mobile.common.domain

import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.domain.BaseUseCase
import kg.optima.mobile.common.domain.model.LaunchInfo
import kg.optima.mobile.core.error.Failure
import kg.optima.mobile.feature.auth.component.AuthPreferences

class LaunchUseCase(
	private val authPreferences: AuthPreferences,
) : BaseUseCase<LaunchUseCase.Params, LaunchInfo>() {

	override suspend fun execute(model: Params): Either<Failure, LaunchInfo> =
		Either.Right(
			LaunchInfo(
				isAuthorized = authPreferences.isAuthorized,
				clientId = authPreferences.clientId,
				grantTypes = authPreferences.grantTypes,
			)
		)

	object Params
}