package kg.optima.mobile.auth.domain.usecase.client_info

import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.domain.BaseUseCase
import kg.optima.mobile.core.error.Failure
import kg.optima.mobile.feature.auth.component.AuthPreferences

class ClientInfoUseCase(
	private val authPreferences: AuthPreferences,
) : BaseUseCase<ClientInfoUseCase.Params, ClientInfo>() {

	override suspend fun execute(model: Params): Either<Failure, ClientInfo> {
		val info = ClientInfo(
			clientId = authPreferences.clientId.orEmpty(),
			isAuthorized = authPreferences.isAuthorized,
			pinEnabled = authPreferences.pin.isNotBlank(),
			biometryEnabled = authPreferences.biometry.isNotBlank(),
		)
		return Either.Right(info)
	}

	object Params
}
