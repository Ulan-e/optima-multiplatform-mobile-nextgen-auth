package kg.optima.mobile.auth.domain.usecase.client_info

import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.domain.BaseUseCase
import kg.optima.mobile.core.error.Failure
import kg.optima.mobile.feature.auth.component.AuthPreferences

class ClientInfoUseCase(
	private val authPreferences: AuthPreferences,
) : BaseUseCase<ClientInfoUseCase.Params, String?>() {

	override suspend fun execute(model: Params): Either<Failure, String?> =
		Either.Right(authPreferences.clientId)

	object Params
}