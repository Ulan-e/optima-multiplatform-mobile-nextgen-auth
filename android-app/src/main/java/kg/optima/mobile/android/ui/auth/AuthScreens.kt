package kg.optima.mobile.android.ui.auth

import androidx.fragment.app.FragmentActivity
import cafe.adriel.voyager.core.registry.ScreenProvider
import kg.optima.mobile.android.ui.RegisterScreens
import kg.optima.mobile.android.ui.auth.login.LoginScreen
import kg.optima.mobile.android.ui.auth.pin.PinEnterScreen
import kg.optima.mobile.android.ui.auth.pin.PinSetScreen
import kg.optima.mobile.auth.presentation.login.LoginStateMachine

sealed interface AuthScreens : ScreenProvider {
	object Login : AuthScreens
	object PinSet : AuthScreens
	class PinEnter(
		val activity: FragmentActivity,
		val initialState: LoginStateMachine.LoginState? = null
	) : AuthScreens
}

val authScreens: RegisterScreens = {
	register<AuthScreens.Login> { LoginScreen }
	register<AuthScreens.PinSet> { PinSetScreen }
	register<AuthScreens.PinEnter> { PinEnterScreen(it.activity, it.initialState) }
}
