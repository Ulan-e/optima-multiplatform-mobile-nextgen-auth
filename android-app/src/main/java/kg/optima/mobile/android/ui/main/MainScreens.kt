package kg.optima.mobile.android.ui.main

import cafe.adriel.voyager.core.registry.ScreenProvider
import kg.optima.mobile.android.ui.RegisterScreens

sealed interface MainScreens : ScreenProvider {
	object Main : MainScreens
}

val mainScreens: RegisterScreens = {
	register<MainScreens.Main> { MainScreen }
}
