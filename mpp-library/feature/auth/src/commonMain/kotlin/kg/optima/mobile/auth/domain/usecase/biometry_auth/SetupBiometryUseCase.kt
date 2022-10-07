package kg.optima.mobile.auth.domain.usecase.biometry_auth

import kg.optima.mobile.feature.auth.component.AuthPreferences
import kg.optima.mobile.auth.domain.usecase.pin_set.SetupAuthResult
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.domain.BaseUseCase
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.core.error.Failure


class SetupBiometryUseCase(
	private val authPreferences: AuthPreferences,
) : BaseUseCase<SetupBiometryUseCase.Params, SetupAuthResult>() {

	override suspend fun execute(
		model: Params,
	): Either<Failure, SetupAuthResult> {
		authPreferences.biometry = if (model.accessed) "Biometry auth" else emptyString
		return Either.Right(SetupAuthResult.Done)
	}

	class Params(
		val accessed: Boolean
	)
}