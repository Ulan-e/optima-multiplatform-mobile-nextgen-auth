package kg.optima.mobile.auth.domain.usecase.login

import kg.optima.mobile.auth.data.api.model.login.UserAuthenticationRequest
import kg.optima.mobile.feature.auth.component.AuthPreferences
import kg.optima.mobile.auth.data.repository.AuthRepository
import kg.optima.mobile.auth.presentation.login.model.LoginModel
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.domain.BaseUseCase
import kg.optima.mobile.core.common.CryptographyUtils
import kg.optima.mobile.core.error.Failure
import kg.optima.mobile.network.const.NetworkCode

/* TODO
   1. Refactor saving params with throwing errors and handling them.
   2. Is saving token model to storage in UseCase correct?
*/
class LoginUseCase(
	private val authRepository: AuthRepository,
	private val authPreferences: AuthPreferences,
) : BaseUseCase<LoginUseCase.Params, LoginModel.SignInResult>() {

	override suspend fun execute(
		model: Params,
	): Either<Failure, LoginModel.SignInResult> {
		return when (model) {
			is Params.Biometry -> signIn()
			is Params.Password -> signIn(model.clientId, model.password, model.smsCode)
			is Params.Pin -> {
				if (model.pin == authPreferences.pin) {
					signIn()
				} else {
					Either.Left(Failure.Message("Неверный pin"))
				}
			}
		}
	}

	private suspend fun signIn(
		clientId: String = authPreferences.clientId.orEmpty(),
		password: String = authPreferences.password,
		smsCode: String? = null,
	): Either<Failure, LoginModel.SignInResult> {
		val request = UserAuthenticationRequest(
			clientId = clientId,
			password = CryptographyUtils.getHash(password),
			smsCode = smsCode,
		)
		return authRepository.login(request).map {
			when (NetworkCode.byCode(it.code)) {
				NetworkCode.Success -> LoginModel.SignInResult.SuccessAuth(
					firstAuth = !authPreferences.isAuthorized,
					bankId = it.data?.userInfo?.bankId.orEmpty(),
					accessToken = it.data?.accessToken.orEmpty(),
				)
				NetworkCode.SmsCodeRequired -> LoginModel.SignInResult.SmsCodeRequired
				NetworkCode.IncorrectCodeOrPassword ->
					LoginModel.SignInResult.IncorrectData(it.message)
				NetworkCode.UserBlocked -> LoginModel.SignInResult.UserBlocked
				else -> LoginModel.SignInResult.Error
			}
		}
	}

	sealed interface Params {
		class Password(
			val clientId: String,
			val password: String,
			val smsCode: String?
		) : Params

		class Pin(
			val pin: String
		) : Params

		object Biometry : Params
	}
}