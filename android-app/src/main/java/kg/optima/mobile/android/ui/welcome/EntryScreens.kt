package kg.optima.mobile.android.ui.welcome

import cafe.adriel.voyager.core.registry.ScreenProvider
import kg.optima.mobile.android.ui.RegisterScreens

sealed interface EntryScreens : ScreenProvider {
	object Welcome : EntryScreens
}

val entryScreens: RegisterScreens = {
	register<EntryScreens.Welcome> { WelcomeScreen }
}
