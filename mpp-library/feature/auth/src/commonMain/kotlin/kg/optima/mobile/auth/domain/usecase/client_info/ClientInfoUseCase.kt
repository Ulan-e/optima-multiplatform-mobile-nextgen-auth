package kg.optima.mobile.auth.domain.usecase.client_info

import kg.optima.mobile.auth.data.component.FeatureAuthComponent
import kg.optima.mobile.auth.domain.usecase.login.GrantType
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.domain.BaseUseCase
import kg.optima.mobile.core.error.Failure

class ClientInfoUseCase(
	private val component: FeatureAuthComponent,
) : BaseUseCase<ClientInfoUseCase.Params, ClientInfo>() {

	override suspend fun execute(model: Params): Either<Failure, ClientInfo> =
		Either.Right(ClientInfo(
			isAuthorized = component.isAuthorized,
			clientId = component.clientId,
			grantTypes = getGrantTypes(),
		))

	private fun getGrantTypes(): List<GrantType> {
		val grantTypes = mutableListOf<GrantType>()
		if (component.isAuthorized) {
			grantTypes.add(GrantType.Password)
			if (component.pin.isNotBlank()) grantTypes.add(GrantType.Pin)
			if (component.fingerPrint.isNotBlank()) grantTypes.add(GrantType.Biometry)
		}
		return grantTypes
	}

	object Params
}