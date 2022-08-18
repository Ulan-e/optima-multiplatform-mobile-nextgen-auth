package kg.optima.mobile.android.ui

import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.registry.screenModule
import kg.optima.mobile.android.ui.auth.authScreens
import kg.optima.mobile.android.ui.main.mainScreens
import kg.optima.mobile.android.ui.welcome.entryScreens

val screens = screenModule {
	featureScreens.forEach { it.invoke(this) }
}

private val featureScreens = listOf(
	entryScreens,
	authScreens,
	mainScreens,
)

typealias RegisterScreens = ScreenRegistry.() -> Unit