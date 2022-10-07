package kg.optima.mobile.common.presentation.welcome

import kg.optima.mobile.base.presentation.BaseMppIntent


class WelcomeIntent(
	override val mppState: WelcomeState,
) : BaseMppIntent<WelcomeEntity>() {

	fun login() =
		mppState.handle(WelcomeEntity.Login)

	fun register() =
		mppState.handle(WelcomeEntity.Register)
}
