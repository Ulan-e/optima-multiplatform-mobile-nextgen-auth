package kg.optima.mobile.auth.domain.usecase.keep_alive

import kg.optima.mobile.auth.data.repository.AuthRepository
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.domain.BaseUseCase
import kg.optima.mobile.core.error.Failure
import kg.optima.mobile.feature.auth.component.AuthPreferences

class KeepAliveUseCase(
	private val authRepository: AuthRepository,
	private val authPreferences: AuthPreferences,
) : BaseUseCase<Unit, Unit>() {

	override suspend fun execute(model: Unit): Either<Failure, Unit> {
		return keepAlive()
	}

	private suspend fun keepAlive(): Either<Failure, Unit> {
		val sessionId = authPreferences.sessionData?.accessToken
		return if (sessionId != null) {
			authRepository.keepAlive(sessionId).map { resp ->
				if (resp.data != null) {
					authPreferences.sessionData =
						authPreferences.sessionData?.copy(accessToken = resp.data!!)
				} else {
					// TODO what to do when accesstoken is null
				}
			}
		} else {
			Either.Left(Failure.NotFoundFailure)
		}
	}
}