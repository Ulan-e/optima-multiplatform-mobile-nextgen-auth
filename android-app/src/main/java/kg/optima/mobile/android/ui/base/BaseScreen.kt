package kg.optima.mobile.android.ui.base

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import java.lang.System.currentTimeMillis

interface BaseScreen : Screen {
	override val key: ScreenKey
		get() = currentTimeMillis().toString()
}