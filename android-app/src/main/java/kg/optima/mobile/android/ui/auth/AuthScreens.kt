package kg.optima.mobile.android.ui.auth

import cafe.adriel.voyager.core.registry.ScreenProvider
import kg.optima.mobile.android.ui.RegisterScreens
import kg.optima.mobile.android.ui.auth.login.LoginScreen
import kg.optima.mobile.android.ui.auth.pin.PinEnterScreen
import kg.optima.mobile.android.ui.auth.pin.PinSetScreen

sealed interface AuthScreens : ScreenProvider {
	object Login : AuthScreens
	object PinSet : AuthScreens
	class PinEnter(val showBiometry: Boolean) : AuthScreens
}

val authScreens: RegisterScreens = {
	register<AuthScreens.Login> { LoginScreen }
	register<AuthScreens.PinSet> { PinSetScreen }
	register<AuthScreens.PinEnter> { PinEnterScreen(it.showBiometry) }
}
