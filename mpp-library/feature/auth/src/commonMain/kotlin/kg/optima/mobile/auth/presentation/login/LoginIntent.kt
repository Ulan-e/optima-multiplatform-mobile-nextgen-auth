package kg.optima.mobile.auth.presentation.login

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.auth.domain.usecase.client_info.ClientInfoUseCase
import kg.optima.mobile.auth.domain.usecase.login.LoginUseCase
import kg.optima.mobile.auth.presentation.login.model.LoginModel
import kg.optima.mobile.auth.presentation.login.utils.toUseCaseModel
import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.presentation.Intent
import org.koin.core.component.inject

class LoginIntent(
	override val state: LoginState,
) : Intent<LoginModel>() {

	private val loginUseCase: LoginUseCase by inject()
	private val clientInfoUseCase: ClientInfoUseCase by inject()

	fun signIn(info: SignInInfo) {
		launchOperation {
			loginUseCase.execute(info.toUseCaseModel())
		}
	}

	fun getClientId() {
		launchOperation {
			clientInfoUseCase.execute(ClientInfoUseCase.Params).map { LoginModel.ClientId(it) }
		}
	}

	sealed interface SignInInfo : Parcelable {
		@Parcelize
		data class Password(
			val clientId: String,
			val password: String,
			val smsCode: String? = null,
		) : SignInInfo

		@Parcelize
		class Pin(
			val pin: String,
		) : SignInInfo

		@Parcelize
		object Biometry : SignInInfo
	}
}
