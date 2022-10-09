package kg.optima.mobile.auth.domain.usecase.logout

import kg.optima.mobile.auth.data.repository.AuthRepository
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.domain.BaseUseCase
import kg.optima.mobile.core.error.Failure
import kg.optima.mobile.feature.auth.component.AuthPreferences

class LogoutUseCase(
	private val authRepository: AuthRepository,
	private val authPreferences: AuthPreferences,
) : BaseUseCase<LogoutUseCase.Params, Boolean>() {

	override suspend fun execute(model: Params): Either<Failure, Boolean> {
		authPreferences.clearProfile()
		return Either.Right(true)
	}

	class Params
}