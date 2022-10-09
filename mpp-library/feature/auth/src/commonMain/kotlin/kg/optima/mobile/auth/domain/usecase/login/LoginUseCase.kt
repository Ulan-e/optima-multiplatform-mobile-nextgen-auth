package kg.optima.mobile.auth.domain.usecase.login

import kg.optima.mobile.auth.data.api.model.login.UserAuthenticationRequest
import kg.optima.mobile.auth.data.repository.AuthRepository
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.domain.BaseUseCase
import kg.optima.mobile.core.common.CryptographyUtils
import kg.optima.mobile.core.error.Failure
import kg.optima.mobile.feature.auth.component.AuthPreferences
import kg.optima.mobile.feature.auth.component.SessionData
import kg.optima.mobile.feature.auth.component.UserInfo
import kg.optima.mobile.feature.auth.model.AuthOtpModel
import kg.optima.mobile.feature.auth.model.SignInInfo
import kg.optima.mobile.network.const.NetworkCode

/* TODO
   1. Refactor saving params with throwing errors and handling them.
   2. Is saving token model to storage in UseCase correct?
*/
class LoginUseCase(
	private val authRepository: AuthRepository,
	private val authPreferences: AuthPreferences,
) : BaseUseCase<LoginUseCase.Params, LoginSignInResult>() {

	override suspend fun execute(
		model: Params,
	): Either<Failure, LoginSignInResult> {
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
	): Either<Failure, LoginSignInResult> {
		val request = UserAuthenticationRequest(
			clientId = clientId,
			password = CryptographyUtils.getHash(password),
			smsCode = smsCode,
		)
		return authRepository.login(request).map {
			authPreferences.clearProfile()
			when (NetworkCode.byCode(it.code)) {
				NetworkCode.Success -> {
					authPreferences.clientId = clientId
					it.data?.let { data ->
						authPreferences.password = password
						authPreferences.sessionData = SessionData(
							accessToken = data.accessToken,
							startDateTime = data.startDateTime,
							lastUpdate = data.lastUpdate,
							sessionDuration = data.sessionDuration
						)
						authPreferences.userInfo = UserInfo(
							id = data.userInfo.id,
							bankId = data.userInfo.bankId,
							fullName = data.userInfo.fullName,
							firstName = data.userInfo.firstName,
							lastName = data.userInfo.lastName,
							middleName = data.userInfo.middleName,
							login = data.userInfo.login,
							idn = data.userInfo.idn,
							sex = data.userInfo.sex,
							autoEncrypt = data.userInfo.autoEncrypt,
							address = data.userInfo.address,
							phoneNumber = data.userInfo.phoneNumber,
							imageHash = data.userInfo.imageHash
						)
						val isAuthorized = authPreferences.isAuthorized
						authPreferences.isAuthorized = true

						LoginSignInResult.SuccessAuth(firstAuth = !isAuthorized)
					} ?: run {
						LoginSignInResult.Error
					}
				}
				NetworkCode.SmsCodeRequired -> {
					authPreferences.clientId = clientId
					authPreferences.isAuthorized = false

					LoginSignInResult.SmsCodeRequired(
						AuthOtpModel(
							phoneNumber = "+996 556 250 626",
							signInInfo = SignInInfo(clientId, password, smsCode)
						)
					)
				}
				NetworkCode.IncorrectCodeOrPassword ->
					LoginSignInResult.IncorrectData(it.message)
				NetworkCode.UserBlocked -> LoginSignInResult.UserBlocked
				else -> LoginSignInResult.Error
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